package dev.arack.twitterfake.iam.infrastructure.security.config;

import dev.arack.twitterfake.iam.infrastructure.security.filter.JwtTokenSecurityFilter;
import dev.arack.twitterfake.iam.infrastructure.security.components.JwtToken;
import dev.arack.twitterfake.shared.configs.AppProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.ArrayList;
import java.util.List;

@Configuration
@RequiredArgsConstructor
@EnableMethodSecurity
@EnableWebSecurity
public class WebSecurityConfig {

    private final JwtToken tokenUtil;
    private final UserDetailsService userDetailsService;
    private final AppProperties appProperties;

    private static final String[] SWAGGER_UI_AUTH_WHITELIST = { "/v3/api-docs/**", "/swagger-ui/**", "/swagger-ui.html" };
    private static final String[] ENDPOINTS_ROL_INVITED = { "/api/v1/...", "/api/v1/..." };

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(cors -> cors.configurationSource(request -> {
                    CorsConfiguration corsConfig = new CorsConfiguration();

                    List<String> allowedOrigins = new ArrayList<>();
                    String frontendUrl = appProperties.getFrontendUrl();
                    if (frontendUrl != null && !frontendUrl.isBlank()) {
                        allowedOrigins.add(frontendUrl);
                    }

                    corsConfig.setAllowedOrigins(allowedOrigins);
                    corsConfig.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
                    corsConfig.setAllowedHeaders(List.of("Content-Type", "Authorization"));
                    corsConfig.setAllowCredentials(true);
                    return corsConfig;
                }))
                .csrf(AbstractHttpConfigurer::disable)
                .headers(headers -> headers.frameOptions(HeadersConfigurer.FrameOptionsConfig::sameOrigin)) // H2 Console
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    // Public EndPoints
                    http.requestMatchers("/h2-console/**").permitAll();
                    http.requestMatchers("/api/v1/auth/**").permitAll();
                    http.requestMatchers("/oauth2/**").permitAll();
                    http.requestMatchers(HttpMethod.GET, "/actuator/health").permitAll();
                    http.requestMatchers(HttpMethod.GET, SWAGGER_UI_AUTH_WHITELIST).permitAll();
                    http.requestMatchers(HttpMethod.GET, ENDPOINTS_ROL_INVITED).permitAll();
                    // Any other request
                    http.anyRequest().authenticated();
                })
                .oauth2Login(AbstractHttpConfigurer::disable)
                .addFilterBefore(new JwtTokenSecurityFilter(tokenUtil, userDetailsService), BasicAuthenticationFilter.class)
                .build();
    }
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder);
        provider.setUserDetailsService(userDetailsService);
        return provider;
    }
    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }
}
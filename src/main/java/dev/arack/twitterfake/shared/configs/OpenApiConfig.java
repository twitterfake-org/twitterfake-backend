package dev.arack.twitterfake.shared.configs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.http.HttpHeaders;

@OpenAPIDefinition(
        info = @Info(
                title = "Demo - Twitter Fake API Backend",
                description = "This API provides endpoints for user authentication, post management, and following functionality, modeled after Twitter.",
                termsOfService = "https://t-enlace.example.com/terms_and_conditions",
                version = "1.0.0",
                contact = @Contact(name = "Jack Arana Ramos", url = "https://arack-rs.web.app/", email = "arack.rs@gmail.com"),
                license = @License(name = "Standard Software Use License for TwitterFakeAPI", url = "https://twitterfakeapi.example.com/license")
        ),
        security = @SecurityRequirement(name = "Bearer Authentication")
)
@SecurityScheme(
        name = "Bearer Authentication",
        description = "JWT Bearer Token Authentication for accessing the API.",
        type = SecuritySchemeType.HTTP,
        paramName = HttpHeaders.AUTHORIZATION,
        in = SecuritySchemeIn.HEADER,
        scheme = "bearer",
        bearerFormat = "JWT"
)
public class OpenApiConfig {  }

package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.ports.output.IRolePersistencePort;
import dev.arack.enlace.iam.application.ports.output.IUserPersistencePort;
import dev.arack.enlace.iam.domain.model.RoleEntity;
import dev.arack.enlace.iam.domain.model.RoleEnum;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.LoginRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.SignupRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.AuthResponse;
import dev.arack.enlace.shared.domain.exceptions.ResourceNotFoundException;
import dev.arack.enlace.shared.infrastructure.jwt.utils.JwtUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final IRolePersistencePort rolePersistencePort;
    private final IUserPersistencePort userPersistencePort;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userPersistencePort.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        List<SimpleGrantedAuthority> authorityList = newAuthorities(userEntity);

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.isEnabled(),
                userEntity.isAccountNoExpired(),
                userEntity.isCredentialNoExpired(),
                userEntity.isAccountNoLocked(),
                authorityList);
    }

    public AuthResponse signup(SignupRequest signupRequest) {
        String username = signupRequest.username();
        String password = signupRequest.password();

        List<RoleEnum> rolesRequest = signupRequest.roleList();
        Set<RoleEntity> roleEntityList = rolePersistencePort.findRoleEntitiesByRoleNameIn(rolesRequest);

        if (roleEntityList.isEmpty()) {
            throw new IllegalArgumentException("The roles specified does not exist.");
        }
        UserEntity userEntity = UserEntity.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roleEntityList)
                .isEnabled(true)
                .accountNoLocked(true)
                .accountNoExpired(true)
                .credentialNoExpired(true)
                .build();

        UserEntity userSaved = userPersistencePort.save(userEntity);
        List<SimpleGrantedAuthority> authorities = newAuthorities(userSaved);
        Authentication authentication = new UsernamePasswordAuthenticationToken(userSaved, null, authorities);
        String accessToken = jwtUtil.createToken(authentication);

        return new AuthResponse(username, "User created successfully", true, accessToken);
    }
    public AuthResponse login(LoginRequest loginRequest) {
        String username = loginRequest.username();
        String password = loginRequest.password();

        Authentication authentication = this.authenticate(username, password);
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String accessToken = jwtUtil.createToken(authentication);

        return new AuthResponse(username, "User logged successfully", true, accessToken);
    }
    private Authentication authenticate(String username, String password) {
        UserDetails userDetails = this.loadUserByUsername(username);
        if (userDetails == null) {
            throw new BadCredentialsException("Invalid username or password");
        }
        if (!passwordEncoder.matches(password, userDetails.getPassword())) {
            throw new BadCredentialsException("Incorrect Password");
        }
        return new UsernamePasswordAuthenticationToken(username, password, userDetails.getAuthorities());
    }
    private List<SimpleGrantedAuthority> newAuthorities(UserEntity userEntity) {
        List<SimpleGrantedAuthority> authorityList = new ArrayList<>();
        userEntity.getRoles()
                .forEach(role -> authorityList.add(new SimpleGrantedAuthority("ROLE_".concat(role.getRoleName().name()))));
        userEntity.getRoles().stream()
                .flatMap(role -> role.getPermissionList().stream())
                .forEach(permission -> authorityList.add(new SimpleGrantedAuthority(permission.getName())));

        return authorityList;
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            String username = (String) authentication.getPrincipal();
            return userPersistencePort.findUserEntityByUsername(username)
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
        else {
            return null;
        }
    }
}
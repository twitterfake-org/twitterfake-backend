package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.ports.output.UserPersistencePort;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.shared.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collections;

@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserPersistencePort userPersistencePort;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userPersistencePort.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_".concat(userEntity.getRole().name()));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(), true, true, true, true, Collections.singleton(authority));//...IMPORTANT !
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userPersistencePort.findByUsername(userDetails.getUsername())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
        else {
            return null;
        }
    }
}

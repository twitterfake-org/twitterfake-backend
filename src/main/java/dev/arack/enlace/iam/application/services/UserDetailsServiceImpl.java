package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.ports.output.UserPersistenceOutput;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.UserRepository;
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

    private final UserPersistenceOutput userPersistenceOutput;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userPersistenceOutput.findByUsername(username);
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_".concat(userEntity.getRole().name()));//...IMPORTANT !

        return new User(userEntity.getUsername(),
                userEntity.getPassword(), true, true, true, true, Collections.singleton(authority));//...IMPORTANT !
    }

    public UserEntity getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            return userPersistenceOutput.findByUsername(userDetails.getUsername());
        }
        else {
            return null;
        }
    }
}

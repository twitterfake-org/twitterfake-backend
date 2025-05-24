package dev.arack.twitterfake.iam.application.core;

import dev.arack.twitterfake.iam.infrastructure.dto.request.SignupRequest;
import dev.arack.twitterfake.iam.infrastructure.dto.request.SocialRequest;
import dev.arack.twitterfake.iam.infrastructure.dto.response.UserResponse;
import dev.arack.twitterfake.iam.application.facade.AuthenticationFacade;
import dev.arack.twitterfake.iam.domain.services.UserService;
import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;
import dev.arack.twitterfake.iam.domain.model.entities.RoleEntity;
import dev.arack.twitterfake.iam.domain.model.entities.UserDetailsEntity;
import dev.arack.twitterfake.iam.domain.model.events.UserCreatedEvent;
import dev.arack.twitterfake.iam.domain.model.valueobject.RoleEnum;
import dev.arack.twitterfake.iam.infrastructure.repository.RoleRepository;
import dev.arack.twitterfake.iam.infrastructure.repository.UserRepository;
import dev.arack.twitterfake.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public void createUser(SignupRequest signupRequest, RoleEnum role, SocialRequest socialRequest) {
        RoleEntity roleUser = getRole(role);

        UserEntity user = UserEntity.builder()
                .username(signupRequest.username())
                .password(signupRequest.password())
                .roles(Set.of(roleUser))
                .build();

        user.setUserDetails(UserDetailsEntity.builder()
                .enabled(true)
                .accountNoExpired(true)
                .accountNoLocked(false)
                .credentialNoExpired(true)
                .build());

        UserEntity userSaved = userRepository.save(user);

        eventPublisher.publishEvent(new UserCreatedEvent(this, userSaved, socialRequest));
    }

    private RoleEntity getRole(RoleEnum role) {
        return roleRepository.findByRoleName(role)
                .orElseThrow(() -> new ResourceNotFoundException("Role not found"));
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<UserEntity> userEntities = userRepository.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();

        for (UserEntity userEntity : userEntities) {
            userResponseList.add(UserResponse.fromEntity(userEntity));
        }
        return userResponseList;
    }

    @Override
    public UserResponse getUserByUsername(String username) {

        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserResponse.fromEntity(userEntity);
    }

    @Override
    public void updateUsername(String username) {

        Long userId = authenticationFacade.getCurrentUser().getId();
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userEntity.setUsername(username);
        userRepository.save(userEntity);
    }

    @Override
    public void deleteUser() {
        Long userId = authenticationFacade.getCurrentUser().getId();
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found" + userId));
        userRepository.delete(userEntity);
    }

    @Override
    public UserResponse getUserCurrent() {
        return UserResponse.fromEntity(authenticationFacade.getCurrentUser());
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        log.info("userEntity username: {}", userEntity.getUsername());

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getUserDetails().isEnabled(),
                userEntity.getUserDetails().isAccountNoExpired(),
                userEntity.getUserDetails().isCredentialNoExpired(),
                userEntity.getUserDetails().isAccountNoLocked(),
                userEntity.getUserDetails().getAuthorities());
    }
}
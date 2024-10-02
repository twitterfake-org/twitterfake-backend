package dev.arack.enlace.iam.application.internal.managers;

import dev.arack.enlace.iam.application.dto.response.UserResponse;
import dev.arack.enlace.iam.application.internal.facade.AuthenticationFacade;
import dev.arack.enlace.iam.application.services.UserService;
import dev.arack.enlace.iam.application.port.persistence.UserPersistence;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.iam.domain.events.UserCreatedEvent;
import dev.arack.enlace.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UsersManager implements UserService {

    private final UserPersistence userPersistence;
    private final ApplicationEventPublisher eventPublisher;
    private final AuthenticationFacade authenticationFacade;

    @Override
    public void createUser(String username, String password) {

        UserEntity userSaved = userPersistence.save(
                UserEntity.fromUsernameAndPassword(username, password)
        );
        eventPublisher.publishEvent(new UserCreatedEvent(this, userSaved));
    }

    @Override
    public List<UserResponse> getAllUsers() {

        List<UserEntity> userEntities = userPersistence.findAll();
        List<UserResponse> userResponseList = new ArrayList<>();

        for (UserEntity userEntity : userEntities) {
            userResponseList.add(UserResponse.of(userEntity));
        }
        return userResponseList;
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        UserEntity userEntity = userPersistence.findUserEntityByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        return UserResponse.of(userEntity);
    }

    @Override
    public void updateUsername(String username) {
//        if (userPersistencePort.existsByUsername(username)) {
//            throw new ResourceNotFoundException("Username already exists");
//        }
        Long userId = authenticationFacade.getCurrentUser().getId();
        UserEntity userEntity = userPersistence.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        userEntity.setUsername(username);
        userPersistence.updateUser(userEntity);
    }

    @Override
    public void deleteUser() {
        Long userId = authenticationFacade.getCurrentUser().getId();
        UserEntity userEntity = userPersistence.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found" + userId));
        userPersistence.deleteUser(userEntity);
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userPersistence.findUserEntityByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("El usuario " + username + " no existe."));

        return new User(userEntity.getUsername(),
                userEntity.getPassword(),
                userEntity.getUserDetails().isEnabled(),
                userEntity.getUserDetails().isAccountNoExpired(),
                userEntity.getUserDetails().isCredentialNoExpired(),
                userEntity.getUserDetails().isAccountNoLocked(),
                userEntity.getAuthorities());
    }
}
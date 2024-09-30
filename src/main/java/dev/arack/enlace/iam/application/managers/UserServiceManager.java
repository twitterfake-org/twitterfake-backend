package dev.arack.enlace.iam.application.managers;

import dev.arack.enlace.iam.application.dto.response.UserResponse;
import dev.arack.enlace.iam.application.facades.AuthenticationFacade;
import dev.arack.enlace.iam.application.port.input.services.UserService;
import dev.arack.enlace.iam.application.port.output.persistence.UserPersistence;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class UserServiceManager implements UserService {

    private final UserPersistence userPersistence;
    private final AuthenticationFacade authenticationFacade;

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
}

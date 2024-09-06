package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.ports.output.UserPersistencePort;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.application.ports.input.UserServicePort;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.UserRequest;
import dev.arack.enlace.shared.domain.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServicePort {

    private final UserPersistencePort userPersistencePort;

    @Override
    public List<UserEntity> getAllUsers() {
        return userPersistencePort.findAll();
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return userPersistencePort.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void updateUser(String username, UserRequest userRequest) {
        UserEntity userEntity = getUserByUsername(username);
        userEntity.setFirstName(userRequest.getFirstName());
        userEntity.setLastName(userRequest.getLastName());

        userPersistencePort.updateUser(userEntity);
    }

    @Override
    public void deleteUser(String username) {
        UserEntity userEntity = getUserByUsername(username);

        userPersistencePort.deleteUser(userEntity);
    }
}

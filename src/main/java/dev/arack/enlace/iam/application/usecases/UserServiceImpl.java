package dev.arack.enlace.iam.application.usecases;

import dev.arack.enlace.iam.application.port.output.persistence.UserPersistencePort;
import dev.arack.enlace.iam.domain.aggregate.UserEntity;
import dev.arack.enlace.iam.application.dto.request.UserRequest;
import dev.arack.enlace.shared.exceptions.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements dev.arack.enlace.iam.application.port.input.services.UserService {

    private final UserPersistencePort UserPersistencePort;

    @Override
    public List<UserEntity> getAllUsers() {
        return UserPersistencePort.findAll();
    }

    @Override
    public UserEntity getUserByUsername(String username) {
        return UserPersistencePort.findUserEntityByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

    @Override
    public void updateUser(Long usedId, UserRequest userRequest) {
        UserEntity userEntity = UserPersistencePort.findById(usedId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserPersistencePort.updateUser(userEntity);
    }

    @Override
    public void deleteUser(Long userId) {
        UserEntity userEntity = UserPersistencePort.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        UserPersistencePort.deleteUser(userEntity);
    }
}

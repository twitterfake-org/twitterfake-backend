package dev.arack.enlace.iam.application.services;

import dev.arack.enlace.iam.application.ports.output.UserPersistenceOutput;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.application.ports.input.UserServiceInput;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.UserRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService implements UserServiceInput {

    private final UserPersistenceOutput userPersistenceOutput;
    private final ModelMapper modelMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public List<UserResponse> getAllUsers() {
        List<UserEntity> userEntities = userPersistenceOutput.findAll();
        return userEntities.stream().map(userEntity -> {
            return modelMapper.map(userEntity, UserResponse.class);
        }).toList();
    }

    @Override
    public UserResponse getUserByUsername(String username) {
        UserEntity userEntity = userPersistenceOutput.findByUsername(username);

        return modelMapper.map(userEntity, UserResponse.class);
    }

    @Override
    public void updateUser(String username, UserRequest userRequest) {
        UserEntity userEntity = userPersistenceOutput.findByUsername(username);
        modelMapper.map(userRequest, userEntity);
        userEntity.setPassword(passwordEncoder.encode(userRequest.getPassword()));

        userPersistenceOutput.updateUser(userEntity);
    }

    @Override
    public void deleteUser(String username) {
        UserEntity userEntity = userPersistenceOutput.findByUsername(username);

        userPersistenceOutput.deleteUser(userEntity);
    }
}

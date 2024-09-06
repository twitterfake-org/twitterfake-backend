package dev.arack.enlace.iam.infrastructure.adapters.input.controllers;

import dev.arack.enlace.iam.application.ports.input.UserServicePort;
import dev.arack.enlace.iam.application.services.UserDetailsServiceImpl;
import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.UserRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Controller", description = "API for user operations")
public class UserControllerAdapter {

    private final UserServicePort userServicePort;
    private final ModelMapper modelMapper;
    private final UserDetailsServiceImpl userDetailsService;

    private Long getCurrentUserId() {
        return userDetailsService.getCurrentUser().getId();
    }

    @GetMapping(value = "")
    @Operation(
            summary = "Get all users",
            description = "Fetches all users in the system"
    )
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserEntity> userEntities = userServicePort.getAllUsers();
        List<UserResponse> userResponses = userEntities.stream()
                .map(userEntity -> modelMapper.map(userEntity, UserResponse.class))
                .toList();

        return ResponseEntity.ok(userResponses);
    }

    @GetMapping(value = "/{username}")
    @Operation(
            summary = "Get a user by username",
            description = "Fetches user details by their username"
    )
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserEntity userEntity = userServicePort.getUserByUsername(username);
        UserResponse userResponse = modelMapper.map(userEntity, UserResponse.class);

        return ResponseEntity.ok(userResponse);
    }

    @PutMapping(value = "")
    @Operation(
            summary = "Update user details",
            description = "Updates an existing user's details"
    )
    public ResponseEntity<String> updateUser(@Valid @RequestBody UserRequest userRequest) {
        Long ID_LOGGED = getCurrentUserId();
        userServicePort.updateUser(ID_LOGGED, userRequest);

        return ResponseEntity.ok("User updated successfully");
    }

    @DeleteMapping(value = "")
    @Operation(
            summary = "Delete a user",
            description = "Deletes a user from the system"
    )
    public ResponseEntity<Void> deleteUser() {
        Long ID_LOGGED = getCurrentUserId();
        userServicePort.deleteUser(ID_LOGGED);

        return ResponseEntity.noContent().build();
    }
}

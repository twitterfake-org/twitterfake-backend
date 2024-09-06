package dev.arack.enlace.iam.infrastructure.adapters.input.controllers;

import dev.arack.enlace.iam.application.ports.input.UserServiceInput;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.request.UserRequest;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/users", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "User Controller", description = "API for user operations")
public class UserController {

    private final UserServiceInput userServiceInput;

    @GetMapping(value = "")
    @Operation(
            summary = "Get all users",
            description = "Fetches all users in the system"
    )
    public ResponseEntity<List<UserResponse>> getAllUsers() {
        List<UserResponse> userResponses = userServiceInput.getAllUsers();
        return ResponseEntity.ok(userResponses);
    }

    @GetMapping(value = "/{username}")
    @Operation(
            summary = "Get a user by username",
            description = "Fetches user details by their username"
    )
    public ResponseEntity<UserResponse> getUserByUsername(@PathVariable String username) {
        UserResponse userResponse = userServiceInput.getUserByUsername(username);
        return ResponseEntity.ok(userResponse);
    }

    @PutMapping(value = "/{username}")
    @Operation(
            summary = "Update user details",
            description = "Updates an existing user's details"
    )
    public ResponseEntity<Void> updateUser(@PathVariable String username, @RequestBody UserRequest userRequest) {
        userServiceInput.updateUser(username, userRequest);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(value = "/{username}")
    @Operation(
            summary = "Delete a user",
            description = "Deletes a user from the system"
    )
    public ResponseEntity<Void> deleteUser(@PathVariable String username) {
        userServiceInput.deleteUser(username);
        return ResponseEntity.noContent().build();
    }
}

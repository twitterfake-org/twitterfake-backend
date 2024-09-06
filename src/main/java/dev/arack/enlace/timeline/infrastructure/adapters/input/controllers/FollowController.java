package dev.arack.enlace.timeline.infrastructure.adapters.input.controllers;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.UserRepository;
import dev.arack.enlace.shared.domain.exceptions.ResourceNotFoundException;
import dev.arack.enlace.timeline.application.ports.input.FollowServiceInput;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/follow", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Follow Controller", description = "API for follow operations")
public class FollowController {

    private final FollowServiceInput followServiceInput;
    private final UserRepository userRepository;

    @Transactional
    @PostMapping(value = "/follow")
    @Operation(
            summary = "Follow a user",
            description = "Follow a user by providing the follower and followed user IDs"
    )
    public ResponseEntity<String> followUser(@RequestParam Long followerId, @RequestParam Long followedId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("Cannot follow yourself");
        }
        UserEntity follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        UserEntity followed = userRepository.findById(followedId)
                .orElseThrow(() -> new ResourceNotFoundException("Followed user not found"));

        followServiceInput.followUser(follower, followed);

        return ResponseEntity.status(HttpStatus.CREATED).body("User followed successfully");
    }

    @Transactional
    @DeleteMapping(value = "/unfollow")
    @Operation(
            summary = "Unfollow a user",
            description = "Unfollow a user by providing the follower and followed user IDs"
    )
    public ResponseEntity<String> unfollowUser(@RequestParam Long followerId, @RequestParam Long followedId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("Cannot unfollow yourself");
        }
        UserEntity follower = userRepository.findById(followerId)
                .orElseThrow(() -> new ResourceNotFoundException("Follower not found"));
        UserEntity followed = userRepository.findById(followedId)
                .orElseThrow(() -> new ResourceNotFoundException("Followed user not found"));

        followServiceInput.unfollowUser(follower.getId(), followed.getId());

        return ResponseEntity.ok("User unfollowed successfully");
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/followers/{userId}")
    @Operation(
            summary = "Get followers",
            description = "Get followers of a user by providing the user ID"
    )
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<UserResponse> followersResponse = followServiceInput.getFollowers(user);

        return ResponseEntity.ok(followersResponse);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/following/{userId}")
    @Operation(
            summary = "Get following",
            description = "Get users followed by a user by providing the user ID"
    )
    public ResponseEntity<List<UserResponse>> getFollowing(@PathVariable Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        List<UserResponse> followingResponse = followServiceInput.getFollowing(user);

        return ResponseEntity.ok(followingResponse);
    }
}

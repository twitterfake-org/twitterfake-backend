package dev.arack.enlace.timeline.infrastructure.adapters.input.controllers;

import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.UserRepository;
import dev.arack.enlace.timeline.application.ports.input.FollowServicePort;
import dev.arack.enlace.timeline.domain.model.FollowEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/follow", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Follow Controller", description = "API for follow operations")
public class FollowControllerAdapter {

    private final FollowServicePort followServicePort;
    private final ModelMapper modelMapper;

    @Transactional
    @PostMapping(value = "/follow")
    @Operation(
            summary = "Follow a user",
            description = "Follow a user by providing the follower and followed user IDs"
    )
    public ResponseEntity<String> followUser(@RequestParam Long followerId, @RequestParam Long followedId) {
        followServicePort.followUser(followerId, followedId);

        return ResponseEntity.ok("User followed successfully");
    }

    @Transactional
    @DeleteMapping(value = "/unfollow")
    @Operation(
            summary = "Unfollow a user",
            description = "Unfollow a user by providing the follower and followed user IDs"
    )
    public ResponseEntity<String> unfollowUser(@RequestParam Long followerId, @RequestParam Long followedId) {
        followServicePort.unfollowUser(followerId, followedId);

        return ResponseEntity.ok("User unfollowed successfully");
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/followers/{userId}")
    @Operation(
            summary = "Get followers",
            description = "Get followers of a user by providing the user ID"
    )
    public ResponseEntity<List<UserResponse>> getFollowers(@PathVariable Long userId) {
        List<FollowEntity> followEntities = followServicePort.getFollowers(userId);
        List<UserResponse> followersResponse = followEntities.stream()
                .map(followEntity -> modelMapper.map(followEntity.getFollower(), UserResponse.class))
                .toList();

        return ResponseEntity.ok(followersResponse);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/following/{userId}")
    @Operation(
            summary = "Get following",
            description = "Get users followed by a user by providing the user ID"
    )
    public ResponseEntity<List<UserResponse>> getFollowing(@PathVariable Long userId) {
        List<FollowEntity> followEntities = followServicePort.getFollowing(userId);
        List<UserResponse> followingResponse = followEntities.stream()
                .map(followEntity -> modelMapper.map(followEntity.getFollowed(), UserResponse.class))
                .toList();
        return ResponseEntity.ok(followingResponse);
    }
}

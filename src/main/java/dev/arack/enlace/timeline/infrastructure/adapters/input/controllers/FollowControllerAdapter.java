package dev.arack.enlace.timeline.infrastructure.adapters.input.controllers;

import dev.arack.enlace.iam.application.services.UserDetailsServiceImpl;
import dev.arack.enlace.iam.infrastructure.adapters.input.dto.response.UserResponse;
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
    private final UserDetailsServiceImpl userDetailsService;

    private Long getCurrentUserId() {
        return userDetailsService.getCurrentUser().getId();
    }

    @Transactional
    @PostMapping(value = "/{followedId}")
    @Operation(
            summary = "Follow a user",
            description = "Follow a user by providing the follower and followed user IDs"
    )
    public ResponseEntity<String> followUser(@PathVariable Long followedId) {
        Long ID_LOGGED = getCurrentUserId();
        followServicePort.followUser(ID_LOGGED, followedId);

        return ResponseEntity.ok("User followed successfully");
    }

    @Transactional
    @DeleteMapping(value = "/unfollow/{followedId}")
    @Operation(
            summary = "Unfollow a user",
            description = "Unfollow a user by providing the follower and followed user IDs"
    )
    public ResponseEntity<String> unfollowUser(@PathVariable Long followedId) {
        Long ID_LOGGED = getCurrentUserId();
        followServicePort.unfollowUser(ID_LOGGED, followedId);

        return ResponseEntity.ok("User unfollowed successfully");
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/followers")
    @Operation(
            summary = "Get followers",
            description = "Get followers of a user by providing the user ID"
    )
    public ResponseEntity<List<UserResponse>> getFollowers() {
        Long ID_LOGGED = getCurrentUserId();
        List<FollowEntity> followEntities = followServicePort.getFollowers(ID_LOGGED);
        List<UserResponse> followersResponse = followEntities.stream()
                .map(followEntity -> modelMapper.map(followEntity.getFollower(), UserResponse.class))
                .toList();

        return ResponseEntity.ok(followersResponse);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/following")
    @Operation(
            summary = "Get following",
            description = "Get users followed by a user by providing the user ID"
    )
    public ResponseEntity<List<UserResponse>> getFollowing() {
        Long ID_LOGGED = getCurrentUserId();
        List<FollowEntity> followEntities = followServicePort.getFollowing(ID_LOGGED);
        List<UserResponse> followingResponse = followEntities.stream()
                .map(followEntity -> modelMapper.map(followEntity.getFollowed(), UserResponse.class))
                .toList();
        return ResponseEntity.ok(followingResponse);
    }
}

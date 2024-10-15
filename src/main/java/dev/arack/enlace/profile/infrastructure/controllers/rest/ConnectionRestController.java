package dev.arack.enlace.profile.infrastructure.controllers.rest;

import dev.arack.enlace.profile.application.dto.response.FollowResponse;
import dev.arack.enlace.profile.application.port.input.services.ConnectionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/follow", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Follow Controller", description = "API for follow operations")
public class ConnectionRestController {

    private final ConnectionService connectionService;

    @Transactional
    @PostMapping(value = "/{followedId}")
    @Operation(
            summary = "Follow a user",
            description = "Follow a user by providing the follower and followed user IDs"
    )
    public ResponseEntity<HashMap<String, String>> followUser(@PathVariable Long followedId) {

        connectionService.followUser(followedId);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "User followed successfully");

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Transactional
    @DeleteMapping(value = "/unfollow/{followedId}")
    @Operation(
            summary = "Unfollow a user",
            description = "Unfollow a user by providing the follower and followed user IDs"
    )
    public ResponseEntity<HashMap<String, String>> unfollowUser(@PathVariable Long followedId) {

        connectionService.unfollowUser(followedId);
        HashMap<String, String> response = new HashMap<>();
        response.put("message", "User unfollowed successfully");
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/followers/{userId}")
    @Operation(
            summary = "Get followers",
            description = "Get followers of a user by providing the user ID"
    )
    public ResponseEntity<List<FollowResponse>> getFollowers(@PathVariable Long userId) {

        List<FollowResponse> followersResponse = connectionService.getFollowers(userId);
        return ResponseEntity.status(HttpStatus.OK).body(followersResponse);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/following/{userId}")
    @Operation(
            summary = "Get following",
            description = "Get users followed by a user by providing the user ID"
    )
    public ResponseEntity<List<FollowResponse>> getFollowing(@PathVariable Long userId) {

        List<FollowResponse> followingResponse = connectionService.getFollowing(userId);
        return ResponseEntity.status(HttpStatus.OK).body(followingResponse);
    }
}

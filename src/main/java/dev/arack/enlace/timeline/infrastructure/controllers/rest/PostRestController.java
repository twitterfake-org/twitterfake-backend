package dev.arack.enlace.timeline.infrastructure.controllers.rest;

import dev.arack.enlace.timeline.application.port.input.services.PostService;
import dev.arack.enlace.timeline.application.dto.request.PostRequest;
import dev.arack.enlace.timeline.application.dto.response.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Post Controller", description = "API for post operations")
public class PostRestController {

    private final PostService postService;

    @Transactional
    @PostMapping(value = "")
    @Operation(
            summary = "Create a post",
            description = "Create a post by providing the user ID and the post content"
    )
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest) {

        PostResponse postResponse = postService.createPost(postRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/user/{username}")
    @Operation(
            summary = "Get posts by username",
            description = "Get all posts by a user by providing the username"
    )
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) {

        List<PostResponse> postResponseList = postService.getPostsByUsername(username);
        return ResponseEntity.status(HttpStatus.OK).body(postResponseList);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "{userId}")
    @Operation(
            summary = "Get post by ID",
            description = "Get a post by providing the post ID"
    )
    public ResponseEntity<PostResponse> getPostByUserId(@PathVariable Long userId) {

        PostResponse postResponse = postService.getPostById(userId);
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "")
    @Operation(
            summary = "Get all posts",
            description = "Get all posts"
    )
    public ResponseEntity<List<PostResponse>> getAllPosts() {

        List<PostResponse> postResponseList = postService.getAllPosts();
        return ResponseEntity.status(HttpStatus.OK).body(postResponseList);
    }

    @Transactional
    @PutMapping(value = "{id}")
    @Operation(
            summary = "Update post by ID",
            description = "Update a post by providing the post ID and the updated post content"
    )
    public ResponseEntity<PostResponse> updatePostById(@Valid @PathVariable Long id, @RequestBody PostRequest postRequest) {

        PostResponse postResponse = postService.updatePost(id, postRequest);
        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @Transactional
    @DeleteMapping(value = "{id}")
    @Operation(
            summary = "Delete a post",
            description = "Delete a post by providing the post ID"
    )
    public ResponseEntity<Void> deletePost(@Valid @PathVariable Long id) {

        postService.deletePost(id);
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }
}

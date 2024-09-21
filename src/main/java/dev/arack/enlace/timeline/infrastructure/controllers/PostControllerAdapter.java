package dev.arack.enlace.timeline.infrastructure.controllers;

import dev.arack.enlace.iam.application.usecases.UserDetailsServiceImpl;
import dev.arack.enlace.timeline.domain.model.PostEntity;
import dev.arack.enlace.timeline.application.ports.input.PostServicePort;
import dev.arack.enlace.timeline.application.dto.request.PostRequest;
import dev.arack.enlace.timeline.application.dto.response.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.jetbrains.annotations.NotNull;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@Log4j2
@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/posts", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Post Controller", description = "API for post operations")
public class PostControllerAdapter {

    private final PostServicePort postServicePort;
    private final ModelMapper modelMapper;
    private final UserDetailsServiceImpl userDetailsService;

    private Long getCurrentUserId() {
        log.info("Getting current user ID {}", userDetailsService.getCurrentUser().getId());
        return userDetailsService.getCurrentUser().getId();
    }

    @Transactional
    @PostMapping(value = "")
    @Operation(
            summary = "Create a post",
            description = "Create a post by providing the user ID and the post content"
    )
    public ResponseEntity<PostResponse> createPost(@Valid @RequestBody PostRequest postRequest) {
        Long ID_LOGGED = getCurrentUserId();
        PostEntity postEntity = postServicePort.createPost(ID_LOGGED, postRequest);
        PostResponse postResponse = modelMapper.map(postEntity, PostResponse.class);
        postResponse.setUsername(postEntity.getUserEntity().getUsername());
        postResponse.setCreatedAt(formattedDate(postEntity));

        return ResponseEntity.status(HttpStatus.CREATED).body(postResponse);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/user/{username}")
    @Operation(
            summary = "Get posts by username",
            description = "Get all posts by a user by providing the username"
    )
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) {
        List<PostEntity> posts = postServicePort.getPostsByUsername(username);
        List<PostResponse> postResponses = mapPostsToResponse(posts);

        return ResponseEntity.status(HttpStatus.OK).body(postResponses);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "{userId}")
    @Operation(
            summary = "Get post by ID",
            description = "Get a post by providing the post ID"
    )
    public ResponseEntity<PostResponse> getPostByUserId(@PathVariable Long userId) {
        PostEntity postEntity = postServicePort.getPostById(userId);
        PostResponse postResponse = modelMapper.map(postEntity, PostResponse.class);
        postResponse.setCreatedAt(formattedDate(postEntity));
        postResponse.setUsername(postEntity.getUserEntity().getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "")
    @Operation(
            summary = "Get all posts",
            description = "Get all posts"
    )
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostEntity> postEntities = postServicePort.getAllPosts();
        List<PostResponse> postsResponse = mapPostsToResponse(postEntities);

        return ResponseEntity.status(HttpStatus.OK).body(postsResponse);
    }

    @Transactional
    @PutMapping(value = "{id}")
    @Operation(
            summary = "Update post by ID",
            description = "Update a post by providing the post ID and the updated post content"
    )
    public ResponseEntity<PostResponse> updatePostById(@Valid @PathVariable Long id, @RequestBody PostRequest postRequest) {
        Long ID_LOGGED = getCurrentUserId();
        PostEntity postEntity = postServicePort.updatePost(id, postRequest.getContent(), ID_LOGGED);
        PostResponse postResponse = modelMapper.map(postEntity, PostResponse.class);
        postResponse.setUsername(postEntity.getUserEntity().getUsername());

        return ResponseEntity.status(HttpStatus.OK).body(postResponse);
    }

    @Transactional
    @DeleteMapping(value = "{id}")
    @Operation(
            summary = "Delete a post",
            description = "Delete a post by providing the post ID"
    )
    public ResponseEntity<Void> deletePost(@Valid @PathVariable Long id) {
        //Long ID_LOGGED = getCurrentUserId();
        postServicePort.deletePost(id);

        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @NotNull
    private String formattedDate(@NotNull PostEntity postEntity) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a · MMM d, yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("America/Lima"));
        return formatter.format(postEntity.getCreatedAt());
    }
    private List<PostResponse> mapPostsToResponse(@NotNull List<PostEntity> posts) {
        return posts.stream().map(
                postEntity -> {
                    PostResponse postResponse = modelMapper.map(postEntity, PostResponse.class);
                    postResponse.setUsername(postEntity.getUserEntity().getUsername());
                    postResponse.setCreatedAt(formattedDate(postEntity));
                    return postResponse;
                }
        ).toList();
    }
}

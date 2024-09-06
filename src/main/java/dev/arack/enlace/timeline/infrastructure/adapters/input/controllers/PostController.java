package dev.arack.enlace.timeline.infrastructure.adapters.input.controllers;

import dev.arack.enlace.timeline.domain.model.PostEntity;
import dev.arack.enlace.timeline.application.ports.input.PostServiceInput;
import dev.arack.enlace.timeline.infrastructure.adapters.input.dto.PostRequest;
import dev.arack.enlace.timeline.infrastructure.adapters.input.dto.PostResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.TimeZone;

@RestController
@RequiredArgsConstructor
@RequestMapping(value = "/api/v1/post", produces = MediaType.APPLICATION_JSON_VALUE)
@Tag(name = "Post Controller", description = "API for post operations")
public class PostController {

    private final PostServiceInput postServiceInput;
    private final ModelMapper modelMapper;

    @Transactional
    @PostMapping(value = "/{id}")
    @Operation(
            summary = "Create a post",
            description = "Create a post by providing the user ID and the post content"
    )
    public ResponseEntity<PostResponse> createPost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        PostEntity postEntity = postServiceInput.createPost(id, postRequest);
        PostResponse postResponse = modelMapper.map(postEntity, PostResponse.class);
        postResponse.setAuthorName(postEntity.getUserEntity().getUsername());
        postResponse.setCreatedAt(formattedDate(postEntity));

        return new ResponseEntity<>(postResponse, HttpStatus.CREATED);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/user/{username}")
    @Operation(
            summary = "Get posts by username",
            description = "Get all posts by a user by providing the username"
    )
    public ResponseEntity<List<PostResponse>> getPostsByUsername(@PathVariable String username) {
        List<PostEntity> posts = postServiceInput.getPostsByUsername(username);
        List<PostResponse> postResponses = mapPostsToResponse(posts);

        return ResponseEntity.ok(postResponses);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "/{id}")
    @Operation(
            summary = "Get post by ID",
            description = "Get a post by providing the post ID"
    )
    public ResponseEntity<PostResponse> getPostById(@PathVariable Long id) {
        PostEntity postEntity = postServiceInput.getPostById(id);
        PostResponse postResponse = modelMapper.map(postEntity, PostResponse.class);
        postResponse.setCreatedAt(formattedDate(postEntity));
        postResponse.setAuthorName(postEntity.getUserEntity().getUsername());

        return ResponseEntity.ok(postResponse);
    }

    @Transactional(readOnly = true)
    @GetMapping(value = "")
    @Operation(
            summary = "Get all posts",
            description = "Get all posts"
    )
    public ResponseEntity<List<PostResponse>> getAllPosts() {
        List<PostEntity> postEntities = postServiceInput.getAllPosts();
        List<PostResponse> postsResponse = mapPostsToResponse(postEntities);
        return ResponseEntity.ok(postsResponse);
    }

    @Transactional
    @PutMapping(value = "/{id}")
    @Operation(
            summary = "Update a post",
            description = "Update a post by providing the post ID and the updated post content"
    )
    public ResponseEntity<PostResponse> updatePost(@PathVariable Long id, @RequestBody PostRequest postRequest) {
        PostEntity updatedPost = postServiceInput.updatePost(id, postRequest);
        PostResponse postResponse = modelMapper.map(updatedPost, PostResponse.class);
        postResponse.setAuthorName(updatedPost.getUserEntity().getUsername());

        return ResponseEntity.ok(postResponse);
    }

    @Transactional
    @DeleteMapping(value = "/{id}")
    @Operation(
            summary = "Delete a post",
            description = "Delete a post by providing the post ID"
    )
    public ResponseEntity<Void> deletePost(@PathVariable Long id) {
        postServiceInput.deletePost(id);
        return ResponseEntity.noContent().build();
    }

    private String formattedDate(PostEntity postEntity) {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a · MMM d, yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("America/Lima"));
        return formatter.format(postEntity.getCreatedAt());
    }
    private List<PostResponse> mapPostsToResponse(List<PostEntity> posts) {
        return posts.stream().map(
                postEntity -> {
                    PostResponse postResponse = modelMapper.map(postEntity, PostResponse.class);
                    postResponse.setAuthorName(postEntity.getUserEntity().getUsername());
                    postResponse.setCreatedAt(formattedDate(postEntity));
                    return postResponse;
                }
        ).toList();
    }
}

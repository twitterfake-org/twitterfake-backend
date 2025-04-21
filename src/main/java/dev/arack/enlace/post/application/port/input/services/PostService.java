package dev.arack.enlace.post.application.port.input.services;

import dev.arack.enlace.post.application.dto.response.PostResponse;
import dev.arack.enlace.post.domain.aggregates.PostEntity;
import dev.arack.enlace.post.application.dto.request.PostRequest;

import java.util.List;

/**
 * Interface for post management services, providing methods to create, retrieve, update, and delete posts.
 */
public interface PostService {
    /**
     * Creates a new post with the specified details.
     *
     * @param postRequest The {@link PostRequest} object containing the details of the post to be created.
     * @return A {@link PostEntity} object representing the created post.
     */
    PostResponse createPost(PostRequest postRequest);

    /**
     * Retrieves a list of all posts.
     *
     * @return A {@link List} of {@link PostResponse} objects representing all posts.
     */
    List<PostResponse> getAllPosts();

    /**
     * Retrieves a list of posts made by a specified user.
     *
     * @param username The username of the user whose posts are to be retrieved.
     * @return A {@link List} of {@link PostResponse} objects representing the posts made by the specified user.
     */
    List<PostResponse> getPostsByUsername(String username);

    /**
     * Retrieves a post by its ID.
     *
     * @param postId The ID of the post to be retrieved.
     * @return A {@link PostResponse} object representing the post with the specified ID.
     */
    PostResponse getPostById(Long postId);

    /**
     * Updates an existing post with the specified details.
     *
     * @param postId The ID of the post to be updated.
     * @param postRequest The new post.
     * @return A {@link PostResponse} object representing the updated post.
     */
    PostResponse updatePost(Long postId, PostRequest postRequest);

    /**
     * Deletes a post by its ID.
     *
     * @param postId The ID of the post to be deleted.
     */
    void deletePost(Long postId);
}

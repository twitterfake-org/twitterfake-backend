package dev.arack.enlace.timeline.application.ports.input;

import dev.arack.enlace.timeline.domain.model.PostEntity;
import dev.arack.enlace.timeline.infrastructure.adapters.input.dto.request.PostRequest;

import java.util.List;

/**
 * Interface for post management services, providing methods to create, retrieve, update, and delete posts.
 */
public interface PostServicePort {
    /**
     * Creates a new post with the specified details.
     *
     * @param postId The ID of the post to be created. This may be used to set an initial value or to be ignored if auto-generated.
     * @param postRequest The {@link PostRequest} object containing the details of the post to be created.
     * @return A {@link PostEntity} object representing the created post.
     */
    PostEntity createPost(Long postId, PostRequest postRequest);

    /**
     * Retrieves a list of all posts.
     *
     * @return A {@link List} of {@link PostEntity} objects representing all posts.
     */
    List<PostEntity> getAllPosts();

    /**
     * Retrieves a list of posts made by a specified user.
     *
     * @param username The username of the user whose posts are to be retrieved.
     * @return A {@link List} of {@link PostEntity} objects representing the posts made by the specified user.
     */
    List<PostEntity> getPostsByUsername(String username);

    /**
     * Retrieves a post by its ID.
     *
     * @param postId The ID of the post to be retrieved.
     * @return A {@link PostEntity} object representing the post with the specified ID.
     */
    PostEntity getPostById(Long postId);

    /**
     * Updates an existing post with the specified details.
     *
     * @param postId The ID of the post to be updated.
     * @param content The new post.
     * @param userId The ID of the user updating the post.
     * @return A {@link PostEntity} object representing the updated post.
     */
    PostEntity updatePost(Long postId, String content, Long userId);

    /**
     * Deletes a post by its ID.
     *
     * @param postId The ID of the post to be deleted.
     */
    void deletePost(Long postId);
}

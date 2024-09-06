package dev.arack.enlace.timeline.application.ports.input;

import dev.arack.enlace.timeline.domain.model.PostEntity;
import dev.arack.enlace.timeline.infrastructure.adapters.input.dto.PostRequest;

import java.util.List;

public interface PostServiceInput {
    PostEntity createPost(Long postId, PostRequest postRequest);
    List<PostEntity> getAllPosts();
    List<PostEntity> getPostsByUsername(String username);
    PostEntity getPostById(Long postId);
    PostEntity updatePost(Long postId, PostRequest postRequest);
    void deletePost(Long postId);
}

package dev.arack.enlace.timeline.application.ports.output;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.timeline.domain.model.PostEntity;

import java.util.List;
import java.util.Optional;

public interface PostPersistenceOutput {
    PostEntity createPost(PostEntity post);
    List<PostEntity> findAll();
    List<PostEntity> findAllPostsByUsername(String username);
    Optional<PostEntity> findById(Long postId);
    PostEntity updatePost(PostEntity post);
    void deleteById(Long postId);
}

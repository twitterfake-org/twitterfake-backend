package dev.arack.enlace.post.application.port.output.persistence;

import dev.arack.enlace.post.domain.aggregates.PostEntity;

import java.util.List;
import java.util.Optional;

public interface PostPersistence {
    PostEntity save(PostEntity post);
    List<PostEntity> findAll();
    List<PostEntity> findAllPostsByUsername(String username);
    Optional<PostEntity> findById(Long postId);
    void deleteById(Long postId);

    boolean existsById(Long postId);
}

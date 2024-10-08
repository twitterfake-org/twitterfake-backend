package dev.arack.enlace.post.infrastructure.adapter.persistence.sql;

import dev.arack.enlace.post.application.port.output.persistence.PostPersistence;
import dev.arack.enlace.post.domain.aggregates.PostEntity;
import dev.arack.enlace.post.infrastructure.repository.jpa.JpaPostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class SqlPostPersistence implements PostPersistence {

    private final JpaPostRepository jpaPostRepository;

    @Override
    public PostEntity save(PostEntity post) {
        return jpaPostRepository.save(post);
    }

    @Override
    public List<PostEntity> findAll() {
        return jpaPostRepository.findAll();
    }

    @Override
    public List<PostEntity> findAllPostsByUsername(String username) {
        return jpaPostRepository.findAllPostsByUsername(username);
    }

    @Override
    public Optional<PostEntity> findById(Long postId) {
        return jpaPostRepository.findById(postId);
    }

    @Override
    public void deleteById(Long postId) {
        jpaPostRepository.deleteById(postId);
    }

    @Override
    public boolean existsById(Long postId) {
        return jpaPostRepository.existsById(postId);
    }
}

package dev.arack.enlace.timeline.infrastructure.adapter.sql;

import dev.arack.enlace.timeline.application.port.output.persistence.PostPersistence;
import dev.arack.enlace.timeline.domain.entities.PostEntity;
import dev.arack.enlace.timeline.infrastructure.repository.PostJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostPersistenceSql implements PostPersistence {

    private final PostJpaRepository postJpaRepository;

    @Override
    public PostEntity save(PostEntity post) {
        return postJpaRepository.save(post);
    }

    @Override
    public List<PostEntity> findAll() {
        return postJpaRepository.findAll();
    }

    @Override
    public List<PostEntity> findAllPostsByUsername(String username) {
        return postJpaRepository.findAllPostsByUsername(username);
    }

    @Override
    public Optional<PostEntity> findById(Long postId) {
        return postJpaRepository.findById(postId);
    }

    @Override
    public void deleteById(Long postId) {
        postJpaRepository.deleteById(postId);
    }

    @Override
    public boolean existsById(Long postId) {
        return postJpaRepository.existsById(postId);
    }
}

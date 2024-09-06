package dev.arack.enlace.timeline.infrastructure.adapters.output;

import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.UserRepository;
import dev.arack.enlace.timeline.application.ports.output.PostPersistenceOutput;
import dev.arack.enlace.timeline.domain.model.PostEntity;
import dev.arack.enlace.timeline.infrastructure.adapters.output.repositories.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class PostPersistenceAdapter implements PostPersistenceOutput {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    @Override
    public PostEntity createPost(PostEntity post) {
        return postRepository.save(post);
    }

    @Override
    public List<PostEntity> findAll() {
        return postRepository.findAll();
    }

    @Override
    public List<PostEntity> findAllPostsByUsername(String username) {
        return postRepository.findAllPostsByUsername(username);
    }

    @Override
    public Optional<PostEntity> findById(Long postId) {
        return postRepository.findById(postId);
    }

    @Override
    public PostEntity updatePost(PostEntity post) {
        return postRepository.save(post);
    }

    @Override
    public void deleteById(Long postId) {
        postRepository.deleteById(postId);
    }
}

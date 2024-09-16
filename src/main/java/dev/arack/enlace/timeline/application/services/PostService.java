package dev.arack.enlace.timeline.application.services;

import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.IUserRepository;
import dev.arack.enlace.shared.domain.exceptions.ResourceNotFoundException;
import dev.arack.enlace.timeline.application.ports.output.PostPersistencePort;
import dev.arack.enlace.timeline.domain.model.PostEntity;
import dev.arack.enlace.timeline.application.ports.input.PostServicePort;
import dev.arack.enlace.timeline.infrastructure.adapters.input.dto.request.PostRequest;
import dev.arack.enlace.timeline.infrastructure.adapters.output.repositories.PostRepository;
import dev.arack.enlace.iam.domain.model.UserEntity;
import jakarta.validation.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PostService implements PostServicePort {

    private final PostRepository postRepository;
    private final IUserRepository IUserRepository;
    private final PostPersistencePort postPersistencePort;

    @Override
    public PostEntity createPost(Long postId, PostRequest postRequest) {
        UserEntity userEntity = IUserRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PostEntity postEntity = new PostEntity();
        postEntity.setContent(postRequest.getContent());
        postEntity.setUserEntity(userEntity);

        return postPersistencePort.createPost(postEntity);
    }

    @Override
    public List<PostEntity> getAllPosts() {
        return postPersistencePort.findAll();
    }

    @Override
    public List<PostEntity> getPostsByUsername(String username) {
        return postPersistencePort.findAllPostsByUsername(username);
    }

    @Override
    public PostEntity getPostById(Long postId) {
        return postPersistencePort.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    @Override
    public PostEntity updatePost(Long postId, String content, Long userId) {
        PostEntity postEntity = postPersistencePort.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        if (!(postEntity.getUserEntity().getId().equals(userId))) {
            throw new ValidationException("User not authorized to update post");
        }
        postEntity.setContent(content);

        return postPersistencePort.updatePost(postEntity);
    }

    @Override
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found");
        }
        postPersistencePort.deleteById(postId);
    }
}

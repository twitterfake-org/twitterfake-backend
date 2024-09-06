package dev.arack.enlace.timeline.application.services;

import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.UserRepository;
import dev.arack.enlace.shared.domain.exceptions.ResourceNotFoundException;
import dev.arack.enlace.timeline.application.ports.output.PostPersistenceOutput;
import dev.arack.enlace.timeline.domain.model.PostEntity;
import dev.arack.enlace.timeline.application.ports.input.PostServiceInput;
import dev.arack.enlace.timeline.infrastructure.adapters.input.dto.PostRequest;
import dev.arack.enlace.timeline.infrastructure.adapters.output.repositories.PostRepository;
import dev.arack.enlace.iam.domain.model.UserEntity;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class PostService implements PostServiceInput {

    private final PostRepository postRepository;
    private final UserRepository userRepository;
    private final PostPersistenceOutput postPersistenceOutput;
    private final ModelMapper modelMapper;

    @Override
    public PostEntity createPost(Long postId, PostRequest postRequest) {
        PostEntity postEntity = new PostEntity();

        UserEntity userEntity = userRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        postEntity.setContent(postRequest.getContent());
        postEntity.setUserEntity(userEntity);

        return postPersistenceOutput.createPost(postEntity);
    }

    @Override
    public List<PostEntity> getAllPosts() {
        return postPersistenceOutput.findAll();
    }

    @Override
    public List<PostEntity> getPostsByUsername(String username) {
        return postPersistenceOutput.findAllPostsByUsername(username);
    }

    @Override
    public PostEntity getPostById(Long postId) {
        return postPersistenceOutput.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post not found"));
    }

    @Override
    public PostEntity updatePost(Long postId, PostRequest postRequest) {
        Optional<PostEntity> optionalPost = postPersistenceOutput.findById(postId);

        if (optionalPost.isPresent()) {
            PostEntity postEntity = optionalPost.get();
            modelMapper.map(postRequest, postEntity);
            return postPersistenceOutput.updatePost(postEntity);
        }
        else {
            throw new RuntimeException("Post not found");
        }
    }

    @Override
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new RuntimeException("Post not found");
        }
        postPersistenceOutput.deleteById(postId);
    }
}

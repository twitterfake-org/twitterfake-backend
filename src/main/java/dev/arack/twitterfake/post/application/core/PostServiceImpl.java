package dev.arack.twitterfake.post.application.core;

import dev.arack.twitterfake.iam.infrastructure.repository.UserRepository;
import dev.arack.twitterfake.post.infrastructure.repository.PostRepository;
import dev.arack.twitterfake.profile.infrastructure.client.UserClient;
import dev.arack.twitterfake.shared.exceptions.ResourceNotFoundException;
import dev.arack.twitterfake.post.infrastructure.dto.response.PostResponse;
import dev.arack.twitterfake.post.domain.model.aggregates.PostEntity;
import dev.arack.twitterfake.post.domain.services.PostService;
import dev.arack.twitterfake.post.infrastructure.dto.request.PostRequest;
import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {

    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final UserClient userClient;

    @Override
    public PostResponse createPost(PostRequest postRequest) {

        Long currentUserId = userClient.getCurrentUser().id();

        UserEntity userEntity = userRepository.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PostEntity postEntity = new PostEntity();
        postEntity.setContent(postRequest.content());
        postEntity.setUserEntity(userEntity);

        postRepository.save(postEntity);
        return PostResponse.fromPostEntity(postEntity);
    }

    @Override
    public List<PostResponse> getAllPosts() {

        List<PostEntity> postEntityList = postRepository.findAll();
        return PostResponse.fromPostEntityList(postEntityList);
    }

    @Override
    public List<PostResponse> getPostsByUsername(String username) {

        List<PostEntity> postEntity = postRepository.findAllPostsByUsername(username);
        return PostResponse.fromPostEntityList(postEntity);
    }

    @Override
    public PostResponse getPostById(Long postId) {

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return PostResponse.fromPostEntity(postEntity);
    }

    @Override
    public PostResponse updatePost(Long postId, PostRequest postRequest) {

        PostEntity postEntity = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postEntity.setContent(postRequest.content());
        return PostResponse.fromPostEntity(postRepository.save(postEntity));
    }

    @Override
    public void deletePost(Long postId) {
        if (!postRepository.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found");
        }
        postRepository.deleteById(postId);
    }
}

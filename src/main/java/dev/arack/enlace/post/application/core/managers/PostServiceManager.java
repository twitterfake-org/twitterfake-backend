package dev.arack.enlace.post.application.core.managers;

import dev.arack.enlace.iam.application.port.output.persistence.UserPersistence;
import dev.arack.enlace.profile.application.port.output.client.UserClient;
import dev.arack.enlace.shared.exceptions.ResourceNotFoundException;
import dev.arack.enlace.post.application.dto.response.PostResponse;
import dev.arack.enlace.post.application.port.output.persistence.PostPersistence;
import dev.arack.enlace.post.domain.aggregates.PostEntity;
import dev.arack.enlace.post.application.port.input.services.PostService;
import dev.arack.enlace.post.application.dto.request.PostRequest;
import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@RequiredArgsConstructor
public class PostServiceManager implements PostService {

    private final UserPersistence userPersistence;
    private final PostPersistence postPersistence;
    private final UserClient userClient;

    @Override
    public PostResponse createPost(PostRequest postRequest) {

        Long currentUserId = userClient.getCurrentUser().id();

        UserEntity userEntity = userPersistence.findById(currentUserId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        PostEntity postEntity = new PostEntity();
        postEntity.setContent(postRequest.content());
        postEntity.setUserEntity(userEntity);

        postPersistence.save(postEntity);
        return PostResponse.fromPostEntity(postEntity);
    }

    @Override
    public List<PostResponse> getAllPosts() {

        List<PostEntity> postEntityList = postPersistence.findAll();
        return PostResponse.fromPostEntityList(postEntityList);
    }

    @Override
    public List<PostResponse> getPostsByUsername(String username) {

        List<PostEntity> postEntity = postPersistence.findAllPostsByUsername(username);
        return PostResponse.fromPostEntityList(postEntity);
    }

    @Override
    public PostResponse getPostById(Long postId) {

        PostEntity postEntity = postPersistence.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        return PostResponse.fromPostEntity(postEntity);
    }

    @Override
    public PostResponse updatePost(Long postId, PostRequest postRequest) {

        PostEntity postEntity = postPersistence.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
        postEntity.setContent(postRequest.content());
        return PostResponse.fromPostEntity(postPersistence.save(postEntity));
    }

    @Override
    public void deletePost(Long postId) {
        if (!postPersistence.existsById(postId)) {
            throw new ResourceNotFoundException("Post not found");
        }
        postPersistence.deleteById(postId);
    }
}

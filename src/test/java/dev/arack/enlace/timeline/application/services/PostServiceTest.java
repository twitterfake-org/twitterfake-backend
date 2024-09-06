package dev.arack.enlace.timeline.application.services;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.iam.infrastructure.adapters.output.repositories.UserRepository;
import dev.arack.enlace.shared.domain.exceptions.ResourceNotFoundException;
import dev.arack.enlace.timeline.application.ports.output.PostPersistencePort;
import dev.arack.enlace.timeline.domain.model.PostEntity;
import dev.arack.enlace.timeline.infrastructure.adapters.input.dto.request.PostRequest;
import dev.arack.enlace.timeline.infrastructure.adapters.output.repositories.PostRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @InjectMocks
    private PostService postService;
    @Mock
    private PostRepository postRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private PostPersistencePort postPersistencePort;

    @Test
    void createPost_Success() {
        // Arrange
        Long postId = 1L;
        PostRequest postRequest = new PostRequest();
        postRequest.setContent("Test content");
        PostEntity postEntity = new PostEntity();
        postEntity.setContent("Test content");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user1");
        when(userRepository.findById(postId)).thenReturn(Optional.of(userEntity));
        when(postPersistencePort.createPost(any(PostEntity.class))).thenReturn(postEntity);

        // Act
        PostEntity createdPost = postService.createPost(postId, postRequest);

        // Assert
        assertNotNull(createdPost);
        assertEquals(postRequest.getContent(), createdPost.getContent());
        verify(userRepository, times(1)).findById(postId);
    }
    @Test
    void createPost_UserNotFound() {
        // Arrange
        Long postId = 1L;
        PostRequest postRequest = new PostRequest();
        when(userRepository.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            postService.createPost(postId, postRequest);
        });
        assertEquals("User not found", exception.getMessage());
        verify(postPersistencePort, never()).createPost(any(PostEntity.class));
    }
    @Test
    void getAllPosts_Success() {
        // Arrange
        List<PostEntity> posts = Arrays.asList(new PostEntity(), new PostEntity());
        when(postPersistencePort.findAll()).thenReturn(posts);

        // Act
        List<PostEntity> allPosts = postService.getAllPosts();

        // Assert
        assertNotNull(allPosts);
        assertEquals(2, allPosts.size());
        verify(postPersistencePort, times(1)).findAll();
    }
    @Test
    void getPostsByUsername_Success() {
        // Arrange
        String username = "testuser";
        List<PostEntity> posts = Arrays.asList(new PostEntity(), new PostEntity());
        when(postPersistencePort.findAllPostsByUsername(username)).thenReturn(posts);

        // Act
        List<PostEntity> userPosts = postService.getPostsByUsername(username);

        // Assert
        assertNotNull(userPosts);
        assertEquals(2, userPosts.size());
        verify(postPersistencePort, times(1)).findAllPostsByUsername(username);
    }
    @Test
    void getPostById_Success() {
        // Arrange
        Long postId = 1L;
        PostEntity postEntity = new PostEntity();
        when(postPersistencePort.findById(postId)).thenReturn(Optional.of(postEntity));

        // Act
        PostEntity foundPost = postService.getPostById(postId);

        // Assert
        assertNotNull(foundPost);
        verify(postPersistencePort, times(1)).findById(postId);
    }
    @Test
    void getPostById_PostNotFound() {
        // Arrange
        Long postId = 1L;
        when(postPersistencePort.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            postService.getPostById(postId);
        });
        assertEquals("Post not found", exception.getMessage());
        verify(postPersistencePort, times(1)).findById(postId);
    }
    @Test
    void updatePost_Success() {
        // Arrange
        Long postId = 1L;
        String newContent = "Updated content";
        Long userId = 1L;
        PostEntity existingPost = new PostEntity();
        existingPost.setContent("Old content");
        PostEntity updatedPost = new PostEntity();
        updatedPost.setContent(newContent);
        when(postPersistencePort.findById(postId)).thenReturn(Optional.of(existingPost));
        when(postPersistencePort.updatePost(any(PostEntity.class))).thenReturn(updatedPost);

        // Act
        PostEntity result = postService.updatePost(postId, newContent, userId);

        // Assert
        assertNotNull(result);
        assertEquals(newContent, result.getContent());
        verify(postPersistencePort, times(1)).findById(postId);
        verify(postPersistencePort, times(1)).updatePost(existingPost);
    }
    @Test
    void updatePost_PostNotFound() {
        // Arrange
        Long postId = 1L;
        String content = "Updated content";
        Long userId = 1L;
        when(postPersistencePort.findById(postId)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(ResourceNotFoundException.class, () -> {
            postService.updatePost(postId, content, userId);
        });
        assertEquals("Post not found", exception.getMessage());
        verify(postPersistencePort, never()).updatePost(any(PostEntity.class));
    }

    @Test
    void deletePost_Success() {
        // Arrange
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(true);

        // Act
        postService.deletePost(postId);

        // Assert
        verify(postPersistencePort, times(1)).deleteById(postId);
    }
    @Test
    void deletePost_PostNotFound() {
        // Arrange
        Long postId = 1L;
        when(postRepository.existsById(postId)).thenReturn(false);

        // Act & Assert
        Exception exception = assertThrows(RuntimeException.class, () -> {
            postService.deletePost(postId);
        });
        assertEquals("Post not found", exception.getMessage());
        verify(postPersistencePort, never()).deleteById(postId);
    }
}

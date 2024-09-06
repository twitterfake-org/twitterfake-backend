package dev.arack.enlace.timeline.infrastructure.adapters.input.controllers;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.timeline.application.ports.input.PostServiceInput;
import dev.arack.enlace.timeline.domain.model.PostEntity;
import dev.arack.enlace.timeline.infrastructure.adapters.input.dto.PostRequest;
import dev.arack.enlace.timeline.infrastructure.adapters.input.dto.PostResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PostControllerTest {
    @InjectMocks
    private PostController postController;
    @Mock
    private PostServiceInput postServiceInput;
    @Mock
    private ModelMapper modelMapper;

    @Test
    void createPost_shouldReturnCreated_whenSuccessful() {
        // Arrange
        Long id = 1L;
        PostRequest postRequest = new PostRequest();
        postRequest.setContent("Test content");
        PostEntity postEntity = new PostEntity();
        postEntity.setId(id);
        postEntity.setContent("Test content");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user1");
        postEntity.setUserEntity(userEntity);
        postEntity.setCreatedAt(new Date());
        PostResponse postResponse = new PostResponse();
        postResponse.setAuthorName("user1");
        postResponse.setCreatedAt("10:00 AM · Sep 4, 2023");
        when(postServiceInput.createPost(id, postRequest)).thenReturn(postEntity);
        when(modelMapper.map(postEntity, PostResponse.class)).thenReturn(postResponse);

        // Act
        ResponseEntity<PostResponse> response = postController.createPost(id, postRequest);

        // Assert
        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(postResponse, response.getBody());
        verify(postServiceInput, times(1)).createPost(id, postRequest);
    }
    @Test
    void getPostsByUsername_shouldReturnPosts_whenSuccessful() {
        // Arrange
        String username = "user1";
        PostEntity postEntity = new PostEntity();
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername(username);
        postEntity.setUserEntity(userEntity);
        postEntity.setCreatedAt(new Date());
        List<PostEntity> postEntities = List.of(postEntity);
        PostResponse postResponse = new PostResponse();
        postResponse.setAuthorName(username);
        postResponse.setCreatedAt("10:00 AM · Sep 4, 2023");
        List<PostResponse> postResponses = List.of(postResponse);
        when(postServiceInput.getPostsByUsername(username)).thenReturn(postEntities);
        when(modelMapper.map(any(PostEntity.class), eq(PostResponse.class))).thenReturn(postResponse);

        // Act
        ResponseEntity<List<PostResponse>> response = postController.getPostsByUsername(username);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postResponses, response.getBody());
        verify(postServiceInput, times(1)).getPostsByUsername(username);
        verify(modelMapper, times(postEntities.size())).map(any(PostEntity.class), eq(PostResponse.class));
    }
    @Test
    void getPostById_shouldReturnPost_whenSuccessful() {
        // Arrange
        Long postId = 1L;
        PostEntity postEntity = new PostEntity();
        postEntity.setId(postId);
        postEntity.setContent("Test content");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user1");
        postEntity.setUserEntity(userEntity);
        postEntity.setCreatedAt(new Date());
        PostResponse postResponse = new PostResponse();
        postResponse.setAuthorName("user1");
        postResponse.setCreatedAt("10:00 AM · Sep 4, 2023");
        when(postServiceInput.getPostById(postId)).thenReturn(postEntity);
        when(modelMapper.map(postEntity, PostResponse.class)).thenReturn(postResponse);

        // Act
        ResponseEntity<PostResponse> response = postController.getPostById(postId);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postResponse, response.getBody());
        verify(postServiceInput, times(1)).getPostById(postId);
    }
    @Test
    void getAllPosts_shouldReturnPosts_whenSuccessful() {
        // Arrange
        List<PostEntity> postEntities = new ArrayList<>(List.of(new PostEntity()));
        PostEntity postEntity = new PostEntity();
        postEntity.setContent("Test content");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user1");
        postEntity.setUserEntity(userEntity);
        postEntity.setCreatedAt(new Date());
        postEntities.set(0, postEntity);
        PostResponse postResponse = new PostResponse();
        postResponse.setAuthorName("user1");
        postResponse.setCreatedAt("10:00 AM · Sep 4, 2023");
        List<PostResponse> postResponses = List.of(postResponse);
        when(postServiceInput.getAllPosts()).thenReturn(postEntities);
        when(modelMapper.map(any(PostEntity.class), eq(PostResponse.class))).thenReturn(postResponse);

        // Act
        ResponseEntity<List<PostResponse>> response = postController.getAllPosts();

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postResponses, response.getBody());
        verify(postServiceInput, times(1)).getAllPosts();
    }
    @Test
    void updatePost_shouldReturnUpdatedPost_whenSuccessful() {
        // Arrange
        Long postId = 1L;
        PostRequest postRequest = new PostRequest();
        postRequest.setContent("Updated content");
        PostEntity postEntity = new PostEntity();
        postEntity.setId(postId);
        postEntity.setContent("Updated content");
        UserEntity userEntity = new UserEntity();
        userEntity.setUsername("user1");
        postEntity.setUserEntity(userEntity);
        postEntity.setCreatedAt(new Date());
        PostResponse postResponse = new PostResponse();
        postResponse.setAuthorName("user1");
        postResponse.setCreatedAt("10:00 AM · Sep 4, 2023");
        when(postServiceInput.updatePost(postId, postRequest)).thenReturn(postEntity);
        when(modelMapper.map(postEntity, PostResponse.class)).thenReturn(postResponse);

        // Act
        ResponseEntity<PostResponse> response = postController.updatePost(postId, postRequest);

        // Assert
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(postResponse, response.getBody());
        verify(postServiceInput, times(1)).updatePost(postId, postRequest);
    }
    @Test
    void deletePost_shouldReturnNoContent_whenSuccessful() {
        Long id = 1L;
        doNothing().when(postServiceInput).deletePost(id);

        ResponseEntity<Void> response = postController.deletePost(id);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        verify(postServiceInput, times(1)).deletePost(id);
    }
}

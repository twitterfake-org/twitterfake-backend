package dev.arack.twitterfake.post.infrastructure.repository.jpa;

import dev.arack.twitterfake.post.domain.aggregates.PostEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface JpaPostRepository extends JpaRepository<PostEntity, Long> {
    @Query("SELECT p FROM PostEntity p WHERE p.userEntity.username = :username")
    List<PostEntity> findAllPostsByUsername(@Param("username") String username);
}

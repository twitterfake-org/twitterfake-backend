package dev.arack.twitterfake.profile.infrastructure.repository.jpa;

import dev.arack.twitterfake.iam.domain.aggregates.UserEntity;
import dev.arack.twitterfake.profile.domain.entities.ConnectionEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface JpaConnectionRepository extends JpaRepository<ConnectionEntity, Long> {
    @Query("SELECT f FROM ConnectionEntity f WHERE f.followed.id = :userId")
    List<ConnectionEntity> findByFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT f FROM ConnectionEntity f WHERE f.follower.id = :userId")
    List<ConnectionEntity> findByFollowingByUserId(@Param("userId") Long userId);

    boolean existsByFollowerAndFollowed(UserEntity follower, UserEntity followed);
    Optional<ConnectionEntity> findByFollowerIdAndFollowedId(Long followerId, Long followedId);
}

package dev.arack.enlace.timeline.infrastructure.adapters.output.repositories;

import dev.arack.enlace.iam.domain.model.UserEntity;
import dev.arack.enlace.timeline.domain.model.FollowEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface FollowRepository extends JpaRepository<FollowEntity, Long> {

    @Query("SELECT f FROM FollowEntity f WHERE f.followed.id = :userId")
    List<FollowEntity> findByFollowersByUserId(@Param("userId") Long userId);

    @Query("SELECT f FROM FollowEntity f WHERE f.follower.id = :userId")
    List<FollowEntity> findByFollowingByUserId(@Param("userId") Long userId);

    boolean existsByFollowerAndFollowed(UserEntity follower, UserEntity followed);
    Optional<FollowEntity> findByFollowerIdAndFollowedId(Long followerId, Long followedId);
}

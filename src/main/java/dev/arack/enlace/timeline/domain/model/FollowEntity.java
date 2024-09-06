package dev.arack.enlace.timeline.domain.model;

import dev.arack.enlace.iam.domain.model.UserEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "follows")
public class FollowEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity follower;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity followed;
}

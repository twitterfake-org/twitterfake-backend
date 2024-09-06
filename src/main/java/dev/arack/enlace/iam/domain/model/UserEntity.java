package dev.arack.enlace.iam.domain.model;

import dev.arack.enlace.timeline.domain.model.FollowEntity;
import dev.arack.enlace.timeline.domain.model.PostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "role", nullable = false)
    private RoleEnum role;

    @OneToMany(targetEntity = FollowEntity.class, fetch = FetchType.LAZY, mappedBy = "follower")
    private Set<FollowEntity> following;

    @OneToMany(targetEntity = FollowEntity.class, fetch = FetchType.LAZY, mappedBy = "followed")
    private Set<FollowEntity> followers;

    @OneToMany(targetEntity = PostEntity.class, fetch = FetchType.LAZY, mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<PostEntity> posts;

    public UserEntity() {
        this.role = RoleEnum.USER;
    }
}

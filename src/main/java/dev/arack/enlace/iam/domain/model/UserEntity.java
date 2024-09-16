package dev.arack.enlace.iam.domain.model;

import dev.arack.enlace.timeline.domain.model.FollowEntity;
import dev.arack.enlace.timeline.domain.model.PostEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "users")
public class UserEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

//    @Column(name = "first_name", nullable = false, length = 50)
//    private String firstName;
//
//    @Column(name = "last_name", nullable = false, length = 50)
//    private String lastName;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_enabled")
    private boolean isEnabled;

    @Column(name = "account_no_expired")
    private boolean accountNoExpired;

    @Column(name = "account_no_locked")
    private boolean accountNoLocked;

    @Column(name = "credential_no_expired")
    private boolean credentialNoExpired;

    @ManyToMany(targetEntity = RoleEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(targetEntity = FollowEntity.class, fetch = FetchType.LAZY, mappedBy = "follower")
    private Set<FollowEntity> following;

    @OneToMany(targetEntity = FollowEntity.class, fetch = FetchType.LAZY, mappedBy = "followed")
    private Set<FollowEntity> followers;

    @OneToMany(targetEntity = PostEntity.class, fetch = FetchType.LAZY, mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<PostEntity> posts;
}

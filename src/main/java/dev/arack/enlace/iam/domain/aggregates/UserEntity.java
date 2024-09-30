package dev.arack.enlace.iam.domain.aggregates;

import dev.arack.enlace.iam.domain.entities.RoleEntity;
import dev.arack.enlace.profile.domain.entity.ProfileEntity;
import dev.arack.enlace.profile.domain.entity.ConnectionEntity;
import dev.arack.enlace.timeline.domain.entities.PostEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.util.Date;
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
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends AbstractAggregateRoot<UserEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false, length = 50)
    private String username;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "is_enabled")
    private boolean enabled;

    @Column(name = "account_no_expired")
    private boolean accountNoExpired;

    @Column(name = "account_no_locked")
    private boolean accountNoLocked;

    @Column(name = "credential_no_expired")
    private boolean credentialNoExpired;

    @ManyToMany(targetEntity = RoleEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.MERGE)
    @JoinTable(name = "user_roles", joinColumns = @JoinColumn(name = "user_id"), inverseJoinColumns = @JoinColumn(name = "role_id"))
    private Set<RoleEntity> roles = new HashSet<>();

    @OneToMany(targetEntity = ConnectionEntity.class, fetch = FetchType.LAZY, mappedBy = "follower")
    private Set<ConnectionEntity> following;

    @OneToMany(targetEntity = ConnectionEntity.class, fetch = FetchType.LAZY, mappedBy = "followed")
    private Set<ConnectionEntity> followers;

    @OneToMany(targetEntity = PostEntity.class, fetch = FetchType.LAZY, mappedBy = "userEntity", cascade = CascadeType.REMOVE)
    private List<PostEntity> posts;

    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
    private ProfileEntity profile;

    @CreatedDate
    @Column(nullable = false, updatable = false)
    protected Date createdAt;

    @LastModifiedDate
    @Column(nullable = false)
    protected Date updatedAt;

    public void setProfile(ProfileEntity profile) {
        profile.setUser(this);
        this.profile = profile;
    }

    public static UserEntity fromUsernamePasswordAndRoles(String username, String password, Set<RoleEntity> roles) {
        return UserEntity.builder()
                .username(username)
                .password(password)
                .roles(roles)
                .enabled(true)
                .accountNoExpired(true)
                .accountNoLocked(false)
                .credentialNoExpired(true)
                .build();
    }
}

package dev.arack.enlace.iam.domain.aggregates;

import dev.arack.enlace.iam.domain.entities.RoleEntity;
import dev.arack.enlace.iam.domain.entities.UserDetailsEntity;
import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
import dev.arack.enlace.profile.domain.aggregates.ProfileEntity;
import dev.arack.enlace.profile.domain.entities.ConnectionEntity;
import dev.arack.enlace.timeline.domain.entities.PostEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.domain.AbstractAggregateRoot;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.*;
import java.util.stream.Stream;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table
@EntityListeners(AuditingEntityListener.class)
public class UserEntity extends AbstractAggregateRoot<UserEntity> {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false, length = 50)
    private String username;

    @Column(nullable = false)
    private String password;

    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    @JoinColumn(referencedColumnName = "id")
    private UserDetailsEntity userDetails;

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
        this.profile = profile;
        profile.setUser(this);
    }

    public static UserEntity fromUsernameAndPassword(String username, String password) {
        return UserEntity.builder()
                .username(username)
                .password(password)
                .roles(Set.of(RoleEntity.builder().roleName(RoleEnum.USER).build()))
                .userDetails(UserDetailsEntity.builder()
                        .enabled(true)
                        .accountNoExpired(true)
                        .accountNoLocked(false)
                        .credentialNoExpired(true)
                        .build())
                .build();
    }
}

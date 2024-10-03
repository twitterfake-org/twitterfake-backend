package dev.arack.enlace.iam.domain.entities;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Getter
@Setter
@Table(name = "user_details")
public class UserDetailsEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private boolean enabled;

    @Column
    private boolean accountNoExpired;

    @Column
    private boolean accountNoLocked;

    @Column
    private boolean credentialNoExpired;

    @OneToOne(mappedBy = "userDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserEntity userEntity;

    public Collection<? extends GrantedAuthority> getAuthorities() {
        return userEntity.getRoles().stream().flatMap(role -> Stream.concat(
                        Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name())),
                        role.getPermissionList().stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                ))
                .toList();
    }
}

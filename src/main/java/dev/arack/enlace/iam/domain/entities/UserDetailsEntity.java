package dev.arack.enlace.iam.domain.entities;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import jakarta.persistence.*;
import lombok.*;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;
import java.util.stream.Stream;

@Log4j2
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

    @OneToOne(mappedBy = "userDetails", cascade = CascadeType.ALL)
    @JoinColumn(nullable = false, unique = true)
    private UserEntity user;

    public void setUser(UserEntity user) {
        this.user = user;
        if (user != null && user.getUserDetails() != this) {
            user.setUserDetails(this);
        }
    }


    public Collection<? extends GrantedAuthority> getAuthorities() {

        return user.getRoles().stream().flatMap(role -> Stream.concat(
                        Stream.of(new SimpleGrantedAuthority("ROLE_" + role.getRoleName().name())),
                        role.getPermissionList().stream()
                                .map(permission -> new SimpleGrantedAuthority(permission.getName()))
                ))
                .toList();
    }
}

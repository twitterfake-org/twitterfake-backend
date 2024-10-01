package dev.arack.enlace.iam.domain.entities;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import jakarta.persistence.*;
import lombok.*;

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

    // Atributos de seguridad
    @Column(name = "is_enabled")
    private boolean enabled;

    @Column(name = "account_no_expired")
    private boolean accountNoExpired;

    @Column(name = "account_no_locked")
    private boolean accountNoLocked;

    @Column(name = "credential_no_expired")
    private boolean credentialNoExpired;

    // Relación inversa
    @OneToOne(mappedBy = "userDetails", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private UserEntity userEntity;
}

package dev.arack.enlace.iam.domain.entity;

import dev.arack.enlace.iam.domain.valueobject.RoleEnum;
import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "roles")
public class RoleEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "role_name", length = 9, nullable = false, unique = true)
    @Enumerated(EnumType.STRING)
    private RoleEnum roleName;

//    @ElementCollection(targetClass = PermissionEntity.class)
//    @CollectionTable(name = "role_permissions", joinColumns = @JoinColumn(name = "role_id"))
    @ManyToMany(targetEntity = PermissionEntity.class, fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(
            name = "role_permissions",
            joinColumns = @JoinColumn(name = "role_id"),
            inverseJoinColumns = @JoinColumn(name = "permission_id")
    )
    private Set<PermissionEntity> permissionList = new HashSet<>();
}
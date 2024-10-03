package dev.arack.enlace.profile.domain.entities;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.shared.model.AuditableModel;
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
@Table
public class ConnectionEntity extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity follower;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity followed;
}

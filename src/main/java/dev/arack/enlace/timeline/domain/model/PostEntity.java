package dev.arack.enlace.timeline.domain.model;

import dev.arack.enlace.iam.domain.aggregate.UserEntity;
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
@Table(name = "posts")
public class PostEntity extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "content", nullable = false)
    private String content;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity userEntity;
}

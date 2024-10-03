package dev.arack.enlace.timeline.domain.entities;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.shared.model.AuditableModel;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.util.TimeZone;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table
public class PostEntity extends AuditableModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @ManyToOne(targetEntity = UserEntity.class)
    private UserEntity userEntity;

    @NotNull
    public String getFormattedCreatedAt() {
        SimpleDateFormat formatter = new SimpleDateFormat("h:mm a · MMM d, yyyy");
        formatter.setTimeZone(TimeZone.getTimeZone("America/Lima"));
        return formatter.format(this.createdAt);
    }
}

package dev.arack.enlace.profile.domain.aggregates;

import dev.arack.enlace.iam.domain.aggregates.UserEntity;
import dev.arack.enlace.profile.domain.valueobject.FullName;
import dev.arack.enlace.profile.domain.valueobject.Address;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import lombok.*;

@Entity
@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FullName fullName;

    @Embedded
    private Address address;

    @Email
    private String email;

    @OneToOne
    @JoinColumn(nullable = false, unique = true)
    private UserEntity user;

    public String getFullName() {
        return fullName.firstName() + " " + fullName.lastName();
    }

    public String getAddress() {
        return String.format("%s %s %s %s %s",
                address.street(), address.number(), address.city(), address.zipCode(), address.country());
    }
}

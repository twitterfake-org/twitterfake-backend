package dev.arack.twitterfake.profile.domain.model.aggregates;

import dev.arack.twitterfake.iam.domain.model.aggregates.UserEntity;
import dev.arack.twitterfake.profile.domain.model.valueobject.FullName;
import dev.arack.twitterfake.profile.domain.model.valueobject.Address;
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

    @Column(name = "photo_url")
    private String photoUrl;

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

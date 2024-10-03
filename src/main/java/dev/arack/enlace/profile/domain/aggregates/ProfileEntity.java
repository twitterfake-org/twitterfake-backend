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
@Table(name = "profiles")
public class ProfileEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Embedded
    private FullName fullName;

    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "street", column = @Column(name = "address_street")),
            @AttributeOverride(name = "number", column = @Column(name = "address_number")),
            @AttributeOverride(name = "city", column = @Column(name = "address_city")),
            @AttributeOverride(name = "zipCode", column = @Column(name = "address_zip_code")),
            @AttributeOverride(name = "country", column = @Column(name = "address_country"))
    })
    private Address address;

    @Email
    private String email;

    @OneToOne
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private UserEntity user;

    public String getFullName() {
        return fullName.firstName() + " " + fullName.lastName();
    }

    public String getAddress() {
        return String.format("%s %s %s %s %s",
                address.street(), address.number(), address.city(), address.zipCode(), address.country());
    }

    public ProfileEntity(FullName fullName) {
        this.fullName = new FullName(fullName.firstName(), fullName.lastName());
        this.email = "";
        this.address = new Address("", "", "", "", "");
    }
}

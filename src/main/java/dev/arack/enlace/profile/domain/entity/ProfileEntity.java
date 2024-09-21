package dev.arack.enlace.profile.domain.entity;

import dev.arack.enlace.profile.domain.valueobject.FullName;
import dev.arack.enlace.profile.domain.valueobject.Address;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@Entity
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

//    @Embedded
//    private EmailAddress email;

//    public void Profile(String firstName, String lastName, String street, String number, String city, String zipCode, String country) {
//        this.name = new FullName(firstName, lastName);
//        this.address = new StreetAddress(street, number, city, zipCode, country);
//    }

    public void updateFullName(String firstName, String lastName) {
        this.fullName = new FullName(firstName, lastName);
    }

//    public void updateEmail(String email) {
//        this.email = new EmailAddress(email);
//    }

//    public void updateAddress(String streetAddress, String city, String zipCode, String country) {
//        this.address = new StreetAddress(streetAddress, city, zipCode, country);
//    }
//
//    public String getFullName() {
//        return name.getFullName();
//    }
//
//    public String getStreetAddress() {
//        return address.getStreetAddress();
//    }
}

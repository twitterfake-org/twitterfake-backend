package dev.arack.enlace.profile.domain.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
        String street,
        String number,
        String city,
        String zipCode,
        String country
) {
//    public Address() {
//        this(null, null, null, null, null);
//    }

    public Address {
        if (street == null) {
            throw new IllegalArgumentException("Street must not be null or blank");
        }

        if (city == null) {
            throw new IllegalArgumentException("City must not be null or blank");
        }

        if (zipCode == null) {
            throw new IllegalArgumentException("Zip code must not be null or blank");
        }

        if (country == null) {
            throw new IllegalArgumentException("Country must not be null or blank");
        }
    }

//    public Address(String street, String city, String zipCode, String country) {
//        this(street, null, city, zipCode, country);
//    }
}
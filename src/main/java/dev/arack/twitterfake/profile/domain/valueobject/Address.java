package dev.arack.twitterfake.profile.domain.valueobject;

import jakarta.persistence.Embeddable;

@Embeddable
public record Address(
        String street,
        String number,
        String city,
        String zipCode,
        String country
) {
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
}
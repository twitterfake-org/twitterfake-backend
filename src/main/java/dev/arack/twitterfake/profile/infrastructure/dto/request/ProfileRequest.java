package dev.arack.twitterfake.profile.infrastructure.dto.request;

public record ProfileRequest(
        String firstName,
        String lastName,
        String email,
        String street,
        String number,
        String city,
        String zipCode,
        String country,
        String photoUrl
) {
}

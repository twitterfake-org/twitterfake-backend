package dev.arack.enlace.profile.application.dto.request;

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

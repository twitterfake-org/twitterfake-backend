package dev.arack.enlace.profile.application.dto;

public record ProfileRequest(
        String firstName,
        String lastName,
        String street,
        String number,
        String city,
        String zipCode,
        String country
) {
}

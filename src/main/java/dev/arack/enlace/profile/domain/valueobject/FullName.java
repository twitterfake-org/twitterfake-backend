package dev.arack.enlace.profile.domain.valueobject;

public record FullName(String firstName, String lastName) {

    public FullName {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }
    }
}
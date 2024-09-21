package dev.arack.enlace.profile.domain.valueobject;

public record FullName(String firstName, String lastName) {
    public FullName() {
        this(null, null);
    }

    public FullName {
        if (firstName == null || firstName.isBlank()) {
            throw new IllegalArgumentException("First name cannot be null or blank");
        }

        if (lastName == null || lastName.isBlank()) {
            throw new IllegalArgumentException("Last name cannot be null or blank");
        }
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }
}
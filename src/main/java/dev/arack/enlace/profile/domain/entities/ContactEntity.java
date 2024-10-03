package dev.arack.enlace.profile.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContactEntity {

    @Id
    private Long id;
    private String email;
    private String phoneNumber;
    private String address;
    private String socialMedia;
}

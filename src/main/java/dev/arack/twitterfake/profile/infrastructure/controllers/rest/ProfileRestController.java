package dev.arack.twitterfake.profile.infrastructure.controllers.rest;

import dev.arack.twitterfake.profile.application.dto.request.ProfileRequest;
import dev.arack.twitterfake.profile.application.dto.response.ProfileResponse;
import dev.arack.twitterfake.profile.application.port.input.services.ProfileService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("api/v1/profile")
@Tag(name = "Profile Controller", description = "API for profile operations")
public class ProfileRestController {

    private final ProfileService profileService;

    @GetMapping(value = "")
    @Operation(
            summary = "Get profile",
            description = "Get the profile of the current user"
    )
    public ResponseEntity<ProfileResponse> getProfile() {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getProfile());
    }

    @PutMapping(value = "")
    @Operation(
            summary = "Update profile",
            description = "Update the profile of the current user"
    )
    public ResponseEntity<ProfileResponse> updateProfile(@Valid @RequestBody ProfileRequest profileRequest) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.updateProfile(profileRequest));
    }

    @DeleteMapping(value = "")
    @Operation(
            summary = "Delete profile",
            description = "Delete the profile of the current user"
    )
    public ResponseEntity<ProfileResponse> deleteProfile() {
        profileService.deleteProfile();
        return ResponseEntity.status(HttpStatus.NO_CONTENT).build();
    }

    @GetMapping(value = "/{username}")
    @Operation(
            summary = "Get profile by username",
            description = "Get the profile of the user with the specified username"
    )
    public ResponseEntity<ProfileResponse> getProfileByUsername(@PathVariable String username) {
        return ResponseEntity.status(HttpStatus.OK).body(profileService.getProfileByUsername(username));
    }
}

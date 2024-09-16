package dev.arack.enlace.iam.infrastructure.adapters.input.dto.request;

import dev.arack.enlace.iam.domain.model.RoleEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.List;

public record SignupRequest(
        @NotBlank String username,
        @NotBlank String password,
        @NotEmpty List<RoleEnum> roleList) {
}

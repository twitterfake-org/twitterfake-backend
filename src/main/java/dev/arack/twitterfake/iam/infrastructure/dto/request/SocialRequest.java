package dev.arack.twitterfake.iam.infrastructure.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class SocialRequest {
    private String firstName;
    private String lastName;
    private String email;
    private String photoUrl;
}

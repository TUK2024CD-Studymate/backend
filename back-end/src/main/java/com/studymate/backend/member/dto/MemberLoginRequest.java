package com.studymate.backend.member.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MemberLoginRequest {
    @NotBlank
    @Email
    private String email;
    @NotBlank
    private String password;
}

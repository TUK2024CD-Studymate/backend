package com.studymate.backend.member.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Memeber login reqeust")
public class MemberLoginRequest {
    @NotBlank
    @Email
    @Schema(description = "사용자 이메일", nullable = false, example = "mr6208@naver.com")
    private String email;
    @NotBlank
    @Schema(description = "사용자 비밀번호", nullable = false, example = "asdf1020")
    private String password;
}

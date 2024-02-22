package com.studymate.backend.member.dto;

import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Part;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Schema(description = "Memeber sign-in reqeust")
public class MemberRequest {
    @Email
    @NotBlank
    @Schema(description = "사용자 이메일", nullable = false, example = "mr6208@naver.com")
    private String email;
    @NotBlank
    @Schema(description = "사용자 비밀번호", nullable = false, example = "asdf1020")
    private String password;
    @NotBlank
    @Schema(description = "사용자 이름", nullable = false, example = "정환")
    private String name;
    @NotNull
    @Schema(description = "사용자 멘토,멘티 여부", nullable = false, example = "MENTOR")
    private Part part;
    @NotNull
    @Schema(description = "사용자 관심분야", nullable = false, example = "PROGRAMMING")
    private Interests interests;
    @NotBlank
    @Schema(description = "사용자 닉네임", nullable = false, example = "정환코딩")
    private String nickname;
    @Schema(description = "사용자의 블로그URL", nullable = true, example = "https://dnfjrdl.tistory.com/category")
    private String blogUrl;
    @Schema(description = "사용자 PR요소", nullable = true, example = "한국공학대학교 SW공모전 대상경험")
    private String publicRelations;
    @NotBlank
    @Schema(description = "사용자 직장", nullable = false, example = "Kaist")
    private String job;
}

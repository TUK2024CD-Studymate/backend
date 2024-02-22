package com.studymate.backend.member.dto;


import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Part;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Schema(description = "Memeber update reqeust")
public class MemberUpdateRequest {
    @NotBlank
    @Schema(description = "사용자가 바꿀 이름", nullable = false, example = "민정")
    private String name;
    @NotNull
    @Schema(description = "사용자가 바꿀 멘토, 멘티여부", nullable = false, example = "MENTEE")
    private Part part;
    @NotNull
    @Schema(description = "사용자가 바꿀 관심분야", nullable = false, example = "ENGLISH")
    private Interests interests;
    @NotBlank
    @Schema(description = "사용자가 바꿀 닉네임", nullable = false, example = "화난사람")
    private String nickname;
    @Schema(description = "사용자의 블로그URL", nullable = true, example = "https://hothoony.tistory.com/890")
    private String blogUrl;
    @Schema(description = "사용자 PR요소", nullable = true, example = "한국공학대학교 SW공모전 대상경험, 해커톤 우수상")
    private String publicRelations;
    @NotBlank
    @Schema(description = "사용자 직장", nullable = false, example = "KAKAO개발자")
    private String job;
}

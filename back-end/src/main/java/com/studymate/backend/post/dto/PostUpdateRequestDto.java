package com.studymate.backend.post.dto;


import com.studymate.backend.member.domain.Category;
import com.studymate.backend.member.domain.Interests;
import io.swagger.v3.oas.annotations.media.Schema;
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
@Schema(description = "Post update request")
public class PostUpdateRequestDto {
    @NotBlank
    @Schema(description = "사용자가 바꿀 게시글 제목", nullable = false, example = "영어 학원 추천")
    private String title;

    @NotBlank
    @Schema(description = "사용자가 바꿀 게시글 내용", nullable = false, example = "요즘 잘나가는 강남 영어학원 추천해주세요!")
    private String content;

    @NotNull
    @Schema(description = "사용자가 바꿀 게시글 카테고리", nullable = false, example = "STUDY")
    private Category category;

    @NotNull
    @Schema(description = "사용자가 바꿀 관심 분야", nullable = false)
    private Interests interests;

    @Schema(description = "스터디 모집 여부 변경 (스터디 카테고리인 경우만 적용)", nullable = true)
    private Boolean recruitmentStatus;

}

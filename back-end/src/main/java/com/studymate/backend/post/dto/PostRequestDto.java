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
@Schema(description = "Post create request")
public class PostRequestDto {

    @NotBlank
    @Schema(description = "게시글 제목", nullable = false, example = "자바 공부")
    private String title;

    @NotBlank
    @Schema(description = "게시글 내용", nullable = false, example = "스프링부트는 어렵다^^")
    private String content;

    @NotNull
    @Schema(description = "게시글 카테고리", nullable = false, example = "STUDY")
    private Category category;

    @NotNull
    @Schema(description = "관심 분야", nullable = false, example = "PROGRAMMING")
    private Interests interests;

    @Schema(description = "스터디 모집 여부 (스터디 카테고리인 경우만 적용)", nullable = true, example = "true")
    private Boolean recruitmentStatus;

}

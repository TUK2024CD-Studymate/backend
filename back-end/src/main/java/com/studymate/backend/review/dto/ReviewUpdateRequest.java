package com.studymate.backend.review.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class ReviewUpdateRequest {
    @NotBlank
    @Schema(description = "리뷰 제목", nullable = false, example = "최악입니다.")
    private String title;
    @NotBlank
    @Schema(description = "리뷰 내용", nullable = false, example = "너무 무례하시고 불친절하네요.")
    private String content;
    @NotNull
    @Schema(description = "별점 수", nullable = false, example = "1")
    private int star;
    @NotNull
    @Schema(description = "문제 해결 여부", nullable = false, example = "false")
    private Boolean isSolved;
    @NotNull
    @Schema(description = "좋아요 여부", nullable = false, example = "false")
    private Boolean heart;
}

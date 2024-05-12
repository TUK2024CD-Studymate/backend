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
@Schema(description = "사용자가 리뷰를 작성할때 필요한 데이터")
public class ReviewCreateRequest {

    @NotBlank
    @Schema(description = "리뷰 제목", nullable = false, example = "잘 배우고 갑니다.")
    private String title;
    @NotBlank
    @Schema(description = "리뷰 내용", nullable = false, example = "도움이 정말 많이 됐어요 하하")
    private String content;
    @NotNull
    @Schema(description = "별점 수", nullable = false, example = "4")
    private int star;
    @NotNull
    @Schema(description = "문제 해결 여부", nullable = false, example = "true")
    private Boolean isSolved;
    @NotNull
    @Schema(description = "좋아요 여부", nullable = false, example = "true")
    private Boolean heart;
}

package com.studymate.backend.post.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
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
    @Schema(description = "사용자가 바꿀 게시글 제목", nullable = false, example = "자바 공부")
    private String title;

    @NotBlank
    @Schema(description = "사용자가 바ㄹ꿀 게시글 내용", nullable = false, example = "스프링부트는 어렵다^^")
    private String content;

    @NotBlank
    @Schema(description = "사용자가 바꿀 게시글 카테고리", nullable = false, example = "코딩")
    private String category;

}

package com.studymate.backend.post.dto;

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

    @NotBlank
    @Schema(description = "게시글 카테고리", nullable = false, example = "코딩")
    private String category;

    @NotNull
    @Schema(description = "회원 ID", nullable = false, example = "정환코딩")
    private Long user_id;
}

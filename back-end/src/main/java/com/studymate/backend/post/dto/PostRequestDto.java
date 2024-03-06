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
@Schema(description = "게시글을 생성할때 필요한 데이터")
public class PostRequestDto {

    @NotBlank
    @Schema(description = "게시글 제목", nullable = false, example = "자바를 공부했습니다.")
    private String title;

    @NotBlank
    @Schema(description = "게시글 내용", nullable = false, example = "자바 객체의 불변성에 대해 대학교 동기와 같이 책을 읽고 공부했어요.")
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

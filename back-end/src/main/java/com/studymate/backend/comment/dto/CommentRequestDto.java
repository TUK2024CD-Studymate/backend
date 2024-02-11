package com.studymate.backend.comment.dto;

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
@Schema(description = "Comment create request")
public class CommentRequestDto {
    @NotBlank
    @Schema(description = "댓글 내용", nullable = false, example = "안녕하시렵니까")
    private String content;

    @Schema(description = "댓글을 남길 게시글", nullable = false, example = "1")
    private Long post_id;


}

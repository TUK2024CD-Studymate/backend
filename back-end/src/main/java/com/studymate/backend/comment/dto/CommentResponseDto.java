package com.studymate.backend.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CommentResponseDto {
    private Long id;

    private String content;

    private String nickname;

    private Long post_id;
}

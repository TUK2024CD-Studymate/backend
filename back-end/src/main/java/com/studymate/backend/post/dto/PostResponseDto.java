package com.studymate.backend.post.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class PostResponseDto {
    private Long id;

    private String title;

    private String content;

    private String createdAt;

    private String nickname;

    private String category;

}

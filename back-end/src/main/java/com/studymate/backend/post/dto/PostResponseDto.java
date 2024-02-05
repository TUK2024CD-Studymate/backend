package com.studymate.backend.post.dto;

import com.studymate.backend.member.domain.Category;
import com.studymate.backend.member.domain.Interests;
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

    private Category category;

    private Interests interests;

    private Boolean recruitmentStatus;

}

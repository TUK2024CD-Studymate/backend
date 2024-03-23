package com.studymate.backend.post.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studymate.backend.member.domain.Category;
import com.studymate.backend.member.domain.Interests;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class PostResponseDto {
    private Long post_id;

    private String title;

    private String content;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    private String nickname;

    private Category category;

    private Integer likeCount;

    private Interests interests;

    private Boolean recruitmentStatus;

    private Long commentCount;

}

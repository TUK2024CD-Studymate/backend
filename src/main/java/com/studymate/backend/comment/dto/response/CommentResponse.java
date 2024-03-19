package com.studymate.backend.comment.dto.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.studymate.backend.comment.domain.Comment;
import com.studymate.backend.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Builder
@Getter
public class CommentResponse {
    private Long comment_id;

    private String content;

    private String nickname;

    private Long post_id;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime createdAt;

    public static CommentResponse toResponse(Comment comment){
        if(comment == null) return null;

        Member member = comment.getMember();
        String nickname = (member != null) ? member.getNickname() : null;

        return CommentResponse.builder()
                .comment_id(comment.getId())
                .content(comment.getContent())
                .nickname(nickname)
                .post_id(comment.getPost().getId())
                .createdAt(comment.getCreatedAt())
                .build();
    }
}

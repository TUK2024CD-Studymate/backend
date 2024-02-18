package com.studymate.backend.comment.dto.response;

import com.studymate.backend.comment.domain.Comment;
import com.studymate.backend.member.domain.Member;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@AllArgsConstructor
@Builder
@Getter
public class CommentResponse {
    private Long id;

    private String content;

    private String nickname;

    private Long post_id;

    public static CommentResponse toResponse(Comment comment){
        if(comment == null) return null;

        Member member = comment.getMember();
        String nickname = (member != null) ? member.getNickname() : null;

        return CommentResponse.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(nickname)
                .post_id(comment.getPost().getId())
                .build();
    }
}

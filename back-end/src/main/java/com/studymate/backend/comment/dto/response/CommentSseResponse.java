package com.studymate.backend.comment.dto.response;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommentSseResponse {
    private String nickname; // 댓글 작성자 닉네임
    private Long postId; // 게시물 ID
}
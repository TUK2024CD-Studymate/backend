package com.studymate.backend.comment.dto.request;

import com.studymate.backend.comment.domain.Comment;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.post.domain.Post;
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
@Schema(description = "Comment create/update request")
public class CommentRequest {

    @NotBlank
    @Schema(description = "댓글 내용", nullable = false, example = "안녕하시렵니까")
    private String content;

    public Comment toEntity(Member member, Post post){
        return Comment.builder()
                .member(member)
                .content(getContent())
                .post(post)
                .build();
    }
}

package com.studymate.backend.comment;

import com.studymate.backend.comment.domain.Comment;
import com.studymate.backend.comment.dto.CommentRequestDto;
import com.studymate.backend.comment.dto.CommentResponseDto;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.post.domain.Post;
import org.springframework.stereotype.Component;

@Component
public class CommentMapper {
    private Post post;

    public Comment toEntity(CommentRequestDto request, Member member, Post post){
        return Comment.builder()
                .member(member)
                .content(request.getContent())
                .post(post)
                .build();
    }

    public CommentResponseDto toResponse(Comment comment){
        if(comment == null) return null;

        Member member = comment.getMember();
        String nickname = (member != null) ? member.getNickname() : null;

        return CommentResponseDto.builder()
                .id(comment.getId())
                .content(comment.getContent())
                .nickname(nickname)
                .post_id(comment.getPost().getId())
                .build();
    }
}

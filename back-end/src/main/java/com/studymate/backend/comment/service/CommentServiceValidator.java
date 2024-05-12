package com.studymate.backend.comment.service;

import com.studymate.backend.comment.domain.Comment;
import com.studymate.backend.comment.repository.CommentRepository;
import com.studymate.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class CommentServiceValidator {
    private final CommentRepository commentRepository;
    public Comment validateCommentOwnerShip(Long comment_id, Member currentMember){
        Comment comment = commentRepository.findById(comment_id)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));
        if(!comment.getMember().equals(currentMember)){
            throw new IllegalArgumentException("본인이 작성한 댓글만 수정 및 삭제가 가능합니다.");
        }
        return comment;
    }

    public void validateCommentBelongsToPost(Long commentId, Long postId) {
        Comment comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new IllegalArgumentException("해당 댓글이 존재하지 않습니다."));

        if (!comment.getPost().getId().equals(postId)) {
            throw new IllegalArgumentException("댓글이 해당 게시글에 속하지 않습니다.");
        }
    }
}

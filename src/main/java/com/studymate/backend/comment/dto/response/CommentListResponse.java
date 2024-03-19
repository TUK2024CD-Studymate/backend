package com.studymate.backend.comment.dto.response;

import com.studymate.backend.comment.domain.Comment;
import com.studymate.backend.comment.repository.CommentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CommentListResponse {
    private final CommentRepository commentRepository;

    public List<CommentResponse> getList(Long postId){
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(CommentResponse::toResponse)
                .collect(Collectors.toList());
    }
}

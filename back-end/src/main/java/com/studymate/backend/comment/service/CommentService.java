package com.studymate.backend.comment.service;


import com.studymate.backend.comment.CommentMapper;
import com.studymate.backend.comment.domain.Comment;
import com.studymate.backend.comment.dto.CommentRequestDto;
import com.studymate.backend.comment.dto.CommentResponseDto;
import com.studymate.backend.comment.repository.CommentRepository;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.post.domain.Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final CommentMapper commentMapper;
    private final MemberService memberService;
    private final CommentServiceValidator serviceValidator;

    // 댓글 생성
    @Transactional
    public CommentResponseDto save(CommentRequestDto requestDto, Post post){
        Member member = memberService.getMember();
        Comment comment = commentMapper.toEntity(requestDto, member, post);
        Comment savedComment = commentRepository.save(comment);
        return commentMapper.toResponse(savedComment);
    }

    // 해당 게시글의 댓글 조회
    public List<CommentResponseDto> list(Long postId){
        List<Comment> comments = commentRepository.findByPostId(postId);
        return comments.stream()
                .map(commentMapper::toResponse)
                .collect(Collectors.toList());
    }

    // 댓글 수정
    @Transactional
    public String updateComment(Long id, CommentRequestDto requestDto){
        Member member = memberService.getMember();
        Comment comment = serviceValidator.validateCommentOwnerShip(id, member);
        comment.update(requestDto.getContent());
        return "success";
    }

    // 댓글 삭제
    @Transactional
    public String deleteComment(Long id){
        Member member = memberService.getMember();
        Comment comment = serviceValidator.validateCommentOwnerShip(id, member);
        commentRepository.delete(comment);
        return "success";
    }
}

package com.studymate.backend.comment.service;


import com.studymate.backend.comment.domain.Comment;
import com.studymate.backend.comment.dto.request.CommentRequest;
import com.studymate.backend.comment.dto.response.CommentListResponse;
import com.studymate.backend.comment.dto.response.CommentResponse;
import com.studymate.backend.comment.dto.response.CommentSseResponse;
import com.studymate.backend.comment.repository.CommentRepository;
import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.notification.service.NotificationService;
import com.studymate.backend.post.domain.Post;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

@RequiredArgsConstructor
@Service
public class CommentService {
    private final CommentRepository commentRepository;
    private final MemberService memberService;
    private final CommentServiceValidator serviceValidator;
    private final CommentListResponse commentListResponse;
    private final NotificationService notificationService;
    private final ProfileImgRepository profileImgRepository;
    // 댓글 생성
    @Transactional
    public CommentResponse save(CommentRequest request, Post post) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        Member member = memberService.getMember();
        Member userInfo = post.getMember();
        Comment comment = request.toEntity(member, post);
        Comment savedComment = commentRepository.save(comment);

        CommentSseResponse commentSseResponse = CommentSseResponse.builder()
                .nickname(comment.getMember().getNickname()) // 댓글 작성자 닉네임
                .post_id(post.getId()) // 게시물 ID
                .commentTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter))
                .build();
        // 게시물 작성자에게 보낼 메시지 생성
        String notificationMessage = member.getNickname() + "님이 댓글을 달았습니다.";

        if (!member.getNickname().contains(userInfo.getNickname())) {
            notificationService.saveNotification(member,userInfo, "comment");
        }
        // 게시물 작성자에게 알림 보내기
        notificationService.customNotify(post.getMember(), commentSseResponse, notificationMessage, "Comment");


        return CommentResponse.toResponse(savedComment, profileImgRepository);
    }

    // 해당 게시글의 댓글 조회
    @Transactional
    public List<CommentResponse> list(Long postId){
        return commentListResponse.getList(postId);
    }

    // 댓글 수정
    @Transactional
    public String updateComment(Long postId, Long commentId, CommentRequest request) {
        serviceValidator.validateCommentBelongsToPost(commentId, postId);
        Member member = memberService.getMember();
        Comment comment = serviceValidator.validateCommentOwnerShip(commentId, member);
        comment.update(request.getContent());
        return "success";
    }

    // 댓글 삭제
    @Transactional
    public String deleteComment(Long postId, Long commentId) {
        serviceValidator.validateCommentBelongsToPost(commentId, postId);
        Member member = memberService.getMember();
        Comment comment = serviceValidator.validateCommentOwnerShip(commentId, member);
        commentRepository.delete(comment);
        return "success";
    }

    // 댓글 수 반환
    @Transactional
    public Long countCommentsByPostId(Long postId) {
        return commentRepository.countByPostId(postId);
    }


}

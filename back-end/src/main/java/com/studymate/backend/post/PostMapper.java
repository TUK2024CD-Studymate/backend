package com.studymate.backend.post;

import com.studymate.backend.comment.service.CommentService;
import com.studymate.backend.member.domain.Category;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.post.domain.Post;
import com.studymate.backend.post.dto.PostRequestDto;
import com.studymate.backend.post.dto.PostResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostMapper {
    private final CommentService commentService;

    public Post toEntity(PostRequestDto request, Member member) {
        return Post.builder()
                .member(member)
                .title(request.getTitle())
                .likeCount(0)
                .content(request.getContent())
                .category(request.getCategory())
                .interests(request.getInterests())
                // 카테고리가 STUDY인 경우에만 recruitmentStatus 설정
                .recruitmentStatus(request.getCategory() == Category.STUDY ? request.getRecruitmentStatus() : null)
                .build();
    }

    public PostResponseDto toResponse(Post post) {
        if (post == null) return null;

        // 댓글 수 조회
        Long commentCount = commentService.countCommentsByPostId(post.getId());
        Member member = post.getMember();
        String nickname = (member != null) ? member.getNickname() : null;

        return PostResponseDto.builder()
                .post_id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .likeCount(post.getLikeCount())
                .createdAt(post.getCreatedAt()) // 날짜 포맷은 필요에 따라 변경
                .interests(post.getInterests())
                .recruitmentStatus(post.getRecruitmentStatus()) // 모집 여부 추가
                .nickname(nickname)
                .commentCount(commentCount) // 댓글 수 추가
                .build();
    }

}

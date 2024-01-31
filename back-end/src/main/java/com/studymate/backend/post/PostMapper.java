package com.studymate.backend.post;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.post.domain.Post;
import com.studymate.backend.post.dto.PostRequestDto;
import com.studymate.backend.post.dto.PostResponseDto;
import org.springframework.stereotype.Component;

@Component
public class PostMapper {

    public Post toEntity(PostRequestDto request, Member member) {
        return Post.builder()
                .member(member)
                .title(request.getTitle())
                .content(request.getContent())
                .category(request.getCategory())
                .build();
    }

    public PostResponseDto toResponse(Post post) {
        if (post == null) return null;

        Member member = post.getMember();
        String nickname = (member != null) ? member.getNickname() : null;


        return PostResponseDto.builder()
                .id(post.getId())
                .title(post.getTitle())
                .content(post.getContent())
                .category(post.getCategory())
                .createdAt(post.getCreatedAt().toString()) // 날짜 포맷은 필요에 따라 변경
                .nickname(nickname)
                .build();
    }

}

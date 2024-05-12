package com.studymate.backend.post.service;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.post.domain.Post;
import com.studymate.backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ServiceValidator {
    private final PostRepository postRepository;

    public Post validatePostOwnership(Long postId, Member currentMember) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 게시글이 없습니다."));
        if (!post.getMember().equals(currentMember)) {
            throw new IllegalArgumentException("본인의 게시글만 수정 및 삭제가 가능합니다.");
        }
        return post;
    }
}

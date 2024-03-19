package com.studymate.backend.heart.service;

import com.studymate.backend.heart.HeartRepository;
import com.studymate.backend.heart.domain.Heart;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.post.domain.Post;
import com.studymate.backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;

    @Transactional
    public String insert(Long id) throws Exception {
        Member member = memberService.getMember();

        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("not found post id"));

        if (heartRepository.findByMemberAndPost(member, post).isPresent()) {
            throw new Exception();
        }
        Heart heart = Heart.builder()
                .post(post)
                .member(member)
                .build();
        heartRepository.save(heart);
        postRepository.addLikeCount(post);

        return "좋아요를 눌렀습니다.";
    }

    @Transactional
    public String delete(Long id) {
        Member member = memberService.getMember();

        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("not found id"));

        Heart heart = heartRepository.findByMemberAndPost(member, post)
                .orElseThrow(() -> new RuntimeException("not found heart"));

        heartRepository.delete(heart);
        postRepository.subLikeCount(post);

        return "좋아요를 취소했습니다.";
    }
}

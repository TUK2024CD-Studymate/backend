package com.studymate.backend.heart.service;

import com.studymate.backend.heart.HeartMapper;
import com.studymate.backend.heart.HeartRepository;
import com.studymate.backend.heart.domain.Heart;
import com.studymate.backend.heart.dto.HeartResponse;
import com.studymate.backend.heart.dto.LikeSseResponse;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.notification.service.NotificationService;
import com.studymate.backend.post.domain.Post;
import com.studymate.backend.post.repository.PostRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@Service
@RequiredArgsConstructor
public class HeartService {
    private final HeartRepository heartRepository;
    private final PostRepository postRepository;
    private final MemberService memberService;
    private final NotificationService notificationService;
    private final HeartMapper heartMapper;

    @Transactional
    public HeartResponse insert(Long id) throws Exception {
        Member member = memberService.getMember();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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

        // 구체적인 알림 데이터 생성
        LikeSseResponse likeSseResponse = LikeSseResponse.builder()
                .nickname(member.getNickname()) // 사용자 닉네임
                .post_id(id) // 게시물 ID
                .likedTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter))
                .build();

        // 게시물 작성자에게 구체적인 알림 데이터 보내기
        notificationService.customNotify(post.getMember(), likeSseResponse, "작성하신 게시글에 좋아요가 달렸습니다.", "Like");

        HeartResponse response = heartMapper.toResponse(heart);
        return response;
    }

    @Transactional
    public HeartResponse delete(Long id) {
        Member member = memberService.getMember();

        Post post = postRepository.findById(id).orElseThrow(() -> new RuntimeException("not found id"));

        Heart heart = heartRepository.findByMemberAndPost(member, post)
                .orElseThrow(() -> new RuntimeException("not found heart"));

        HeartResponse response = heartMapper.toResponse(heart);

        heartRepository.delete(heart);
        postRepository.subLikeCount(post);

        return response;
    }
}

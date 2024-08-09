package com.studymate.backend.notification.service;

import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.notification.domain.Notification;
import com.studymate.backend.notification.dto.response.NotificationResponse;
import com.studymate.backend.notification.repository.EmitterRepository;
import com.studymate.backend.notification.repository.NotificationRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final NotificationRepository notificationRepository;
    private final EmitterRepository emitterRepository;
    private final MemberService memberService;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private static final Long DEFAULT_TIMEOUT = 600L * 1000 * 60;  // 60 minutes

    @Transactional
    public void saveNotification(Member myInfo, Member userInfo, String discrimination) {
        String content = "";

        if (discrimination.equals("comment")) {
            content = myInfo.getNickname() + "님이 게시물에 댓글을 달았습니다.";
        }
        if (discrimination.equals("heart")) {
            content = myInfo.getNickname() + "님이 게시글에 좋아요를 달았습니다.";
        }
        if (discrimination.equals("matching")) {
            content = myInfo.getNickname() + "님이 매칭을 신청했습니다.";
        }

        Notification notification = Notification.builder()
                .member(userInfo)
                .content(content)
                .build();

        notificationRepository.save(notification);

        log.info("알림이 저장된 사용자: {}", userInfo.getNickname());
        log.info("알림 내용: {}", content);
    }

    public SseEmitter subscribe(String userEmail) {
        Member member = memberRepository.findByEmail(userEmail);
        SseEmitter emitter = createEmitter(member);
        sendToClient(member, "EventStream Created. [userId=" + member.getId() + "]", "sse 접속 성공", "접속 여부");
        return emitter;
    }

    public void customNotify(Member member, Object data, String comment, String eventName) {
        sendToClient(member, data, comment, eventName);
    }

    private void sendToClient(Member member, Object data, String comment, String eventName) {
        SseEmitter emitter = emitterRepository.get(member.getId());
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(member.getId()))
                        .name(eventName)
                        .data(data)
                        .comment(comment));
            } catch (IOException e) {
                emitterRepository.deleteById(member.getId());
                emitter.completeWithError(e);
                logger.error("Failed to send notification to userId={}, error={}", member.getId(), e.getMessage());
            }
        }
    }

    private SseEmitter createEmitter(Member member) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(member.getId(), emitter);

        emitter.onCompletion(() -> {
            emitterRepository.deleteById(member.getId());
            logger.info("SSE Emitter for userId={} completed", member.getId());
        });
        emitter.onTimeout(() -> {
            emitterRepository.deleteById(member.getId());
            logger.info("SSE Emitter for userId={} timed out", member.getId());
        });

        return emitter;
    }

    public List<NotificationResponse> getNotification() {
        Member member = memberService.getMember();

        List<Notification> notificationList = notificationRepository.findAllByMember(member);

        return notificationList.stream().map(NotificationResponse::new)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteNotification() {
        Member member = memberService.getMember();

        notificationRepository.deleteAllByMember(member);
    }
}

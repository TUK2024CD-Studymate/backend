package com.studymate.backend.notification.service;

import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.notification.repository.EmitterRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;

@Service
@RequiredArgsConstructor
public class NotificationService {

    private final MemberRepository memberRepository;
    private final EmitterRepository emitterRepository;
    private static final Logger logger = LoggerFactory.getLogger(NotificationService.class);

    private static final Long DEFAULT_TIMEOUT = 600L * 1000 * 60;  // 60 minutes

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
}

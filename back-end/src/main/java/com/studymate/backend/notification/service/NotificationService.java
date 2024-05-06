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

    private static final Long DEFAULT_TIMEOUT = 600L * 1000 * 60;

    public SseEmitter subscribe(Long userId) {
        SseEmitter emitter = createEmitter(userId);

        sendToClient(userId, "EventStream Created. [userId="+ userId + "]", "sse 접속 성공");
        return emitter;
    }

    public <T> void customNotify(Long userId, T data, String comment, String type) {
        try{
            sendToClient(userId, data, comment, type);
            logger.info("Successfully sent notification");
        }
        catch (Exception e){
            logger.error("Failed to send notification");
        }
    }
    public void notify(Long userId, Object data, String comment) {
        sendToClient(userId, data, comment);
    }

    private void sendToClient(Long userId, Object data, String comment) {
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(userId))
                        .name("sse")
                        .data(data)
                        .comment(comment));
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }
    }

    private <T> void sendToClient(Long userId, T data, String comment, String type) {
        SseEmitter emitter = emitterRepository.get(userId);
        if (emitter != null) {
            try {
                emitter.send(SseEmitter.event()
                        .id(String.valueOf(userId))
                        .name(type)
                        .data(data)
                        .comment(comment));
            } catch (IOException e) {
                emitterRepository.deleteById(userId);
                emitter.completeWithError(e);
            }
        }
    }

    private SseEmitter createEmitter(Long userId) {
        SseEmitter emitter = new SseEmitter(DEFAULT_TIMEOUT);
        emitterRepository.save(userId, emitter);

        emitter.onCompletion(() -> emitterRepository.deleteById(userId));
        emitter.onTimeout(() -> emitterRepository.deleteById(userId));

        return emitter;
    }

    private Member validUser(Long userId) {
        return memberRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("멤버 ID " + userId + "에 해당하는 유저를 찾을 수 없습니다."));
    }

}
package com.studymate.backend.notification.controller;

import com.studymate.backend.config.security.jwt.TokenProvider;
import com.studymate.backend.notification.dto.response.NotificationResponse;
import com.studymate.backend.notification.service.NotificationService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;


@RestController
@RequiredArgsConstructor
@Slf4j
@Tag(name = "알림", description = "알림 API")
public class NotificationController {
    private final NotificationService notificationService;
    private final TokenProvider tokenProvider;
    @GetMapping(value = "/api/subscribe/{token}", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public ResponseEntity<SseEmitter> subscribe(@PathVariable String token) {
        if (!tokenProvider.validateToken(token)) { // 토큰 검증
            log.info("연결 시 유효하지 않은 토큰입니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userEmail = tokenProvider.getEmailFromToken(token); // 토큰에서 이메일 추출
        if (userEmail == null || userEmail.isEmpty()) {
            log.info("연결 시 인증 정보가 필요합니다.");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        return ResponseEntity.ok(notificationService.subscribe(userEmail));
    }

    @GetMapping("/api/notification")
    @Operation(summary = "알림 조회", description = "자신에게 저장된 알림을 조회한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<List<NotificationResponse>> getNotification() {
        return ResponseEntity.ok().body(notificationService.getNotification());
    }

    @DeleteMapping("/api/notification")
    @Operation(summary = "알림 삭제", description = "쌓인 알림을 모두 삭제한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public void deleteNotification() {
        notificationService.deleteNotification();
    }
}

package com.studymate.backend.global.stomp;

import com.studymate.backend.chat.entity.ChatParticipation;
import com.studymate.backend.config.security.jwt.TokenProvider;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.domain.UserDetail;
import com.studymate.backend.member.service.CustomUserDetailsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    private TokenProvider tokenProvider;
    private CustomUserDetailsService customUserDetailsService;

    public Message<?> preSend(Message<?> message, MessageChannel channel){
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        log.info("ㅎㅇ");
        // websocket 연결했을 때 헤더의 jwt token 유효성 검증
        if(StompCommand.CONNECT == accessor.getCommand()){
            final String authorization = tokenProvider.extractJwt(accessor);

//            tokenProvider.validateToken(authorization);

            if(tokenProvider.validateToken(authorization)){
                String username = tokenProvider.extractUsername(authorization);
                UserDetail userDetail = (UserDetail) customUserDetailsService.loadUserByUsername(username);

                Objects.requireNonNull(accessor.getSessionAttributes()).put("UserDetail", userDetail);
            }
        }
        return message;
    }
}

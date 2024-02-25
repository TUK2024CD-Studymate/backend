package com.studymate.backend.global.stomp;

import com.studymate.backend.chat.entity.ChatParticipation;
import com.studymate.backend.config.security.jwt.TokenProvider;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.CustomUserDetailsService;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;

public class StompHandler implements ChannelInterceptor {
    private TokenProvider tokenProvider;
    private ChatParticipation chatParticipation;
    private CustomUserDetailsService customUserDetailsService;

    public Message<?> preSend(Message<?> message, MessageChannel channel){
        final StompHeaderAccessor accessor = StompHeaderAccessor.wrap(message);

        // websocket 연결했을 때 헤더의 jwt token 유효성 검증
        if(StompCommand.CONNECT == accessor.getCommand()){
            final String authorization = tokenProvider.extractJwt(accessor);

            if(tokenProvider.validateToken(authorization)){
                String username = tokenProvider.extractUsername(authorization);
                Member member = (Member) customUserDetailsService.loadUserByUsername(username);

            }
        }
        return message;
    }
}

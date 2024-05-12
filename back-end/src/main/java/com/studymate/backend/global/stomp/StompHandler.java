package com.studymate.backend.global.stomp;

import com.studymate.backend.config.security.jwt.TokenProvider;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
@Order(Ordered.HIGHEST_PRECEDENCE + 99)
public class StompHandler implements ChannelInterceptor {
    private final MemberRepository memberRepository;
    @Autowired
    private final TokenProvider tokenProvider;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);
        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String authHeader = accessor.getFirstNativeHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                log.warn("Unauthorized attempt to connect without a valid authorization header");
                return (Message<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No valid authorization header provided");
            }
            String token = authHeader.substring(7);
            Authentication authentication = tokenProvider.getAuthentication(token);
            if (authentication == null || !authentication.isAuthenticated()) {
                log.warn("Unauthorized attempt to connect with an invalid token");
                return (Message<?>) ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid token");
            }
            User principal = (User) authentication.getPrincipal();
            String email = principal.getUsername();
            Member member = memberRepository.findByEmail(email);
            accessor.setUser(authentication);
        }
        return message;
    }
}

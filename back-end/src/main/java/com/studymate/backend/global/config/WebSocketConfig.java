package com.studymate.backend.global.config;

import com.studymate.backend.global.stomp.StompExceptionHandler;
import com.studymate.backend.global.stomp.StompHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@RequiredArgsConstructor
@EnableWebSocketMessageBroker
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {

    private final StompHandler stompHandler;
    private final StompExceptionHandler stompExceptionHandler;

    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry.setErrorHandler(stompExceptionHandler)
                .addEndpoint("/ws/chat")
                .setAllowedOriginPatterns("*");
        //.withSockJS()
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {

        registry.enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
                .setRelayHost("rabbitmq")
                .setRelayPort(61613)
                .setClientLogin("guest")
                .setClientPasscode("guest");
        registry.setApplicationDestinationPrefixes("/pub");
        registry.setPathMatcher(new AntPathMatcher("."));
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        registration.interceptors(stompHandler);
    }
}

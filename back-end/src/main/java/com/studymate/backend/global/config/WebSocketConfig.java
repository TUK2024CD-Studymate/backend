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
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketMessageBrokerConfigurer {
    private final StompHandler stompHandler;
    private final StompExceptionHandler stompExceptionHandler;

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
//        registry
//                .enableStompBrokerRelay("/queue", "/topic", "/exchange", "/amq/queue")
//                .setRelayHost("rabbitmq")
//                .setRelayPort(61613)
//                .setClientLogin("guest")
//                .setClientPasscode("guest");
        registry.enableSimpleBroker("/sub");
        registry.setApplicationDestinationPrefixes("/pub");

//        registry.setPathMatcher(new AntPathMatcher("."));
    }
    @Override
    public void registerStompEndpoints (StompEndpointRegistry registry){
        registry.addEndpoint("/ws-stomp")
//                .addInterceptors()
                .setAllowedOriginPatterns("*")
                .withSockJS();
        //.setErrorHandler(stompExceptionHandler)

    }

    /*어플리케이션 내부에서 사용할 path를 지정할 수 있음*/


//    @Override
//    public void configureClientInboundChannel(ChannelRegistration registration) {
//        registration.interceptors(stompHandler);
//    }

}

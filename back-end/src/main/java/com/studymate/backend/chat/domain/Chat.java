package com.studymate.backend.chat.domain;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@AllArgsConstructor
public class Chat {

    public enum MessageType{
        ENTER, TALK
    }
    private MessageType type;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chat_id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "room_id")
    private ChatRoom room;

    private String sender;

//    @Column(columnDefinition = "TEXT")
    private String message;

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime sendDate;

//    public static Chat createChat(ChatRoom room, String sender, String message) {
//        return Chat.builder()
//                .room(room)
//                .sender(sender)
//                .message(message)
//                .build();
//    }

}

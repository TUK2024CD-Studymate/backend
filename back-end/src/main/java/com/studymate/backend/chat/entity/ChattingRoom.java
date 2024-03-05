package com.studymate.backend.chat.entity;

import com.studymate.backend.global.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Getter
@Builder
@AllArgsConstructor(access = AccessLevel.PROTECTED)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class ChattingRoom extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "chattingroom_id")
    private Long id;

    @OneToMany(mappedBy = "chattingRoom", cascade = CascadeType.ALL)
    private List<ChatMessage> chatMessageList;

    @OneToMany(mappedBy = "chattingRoom", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private final List<ChatParticipation> chatParticipationList = new ArrayList<>();


}

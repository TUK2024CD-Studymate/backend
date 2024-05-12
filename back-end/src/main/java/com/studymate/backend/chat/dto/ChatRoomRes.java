package com.studymate.backend.chat.dto;



import lombok.*;


import java.util.List;

@Getter
@Setter
@Builder
public class ChatRoomRes {
    private Long chatRoomId;
    private String chatRoomName;
    private List<MemberDetail> members; // 채팅방 멤버 정보

    @Getter
    @Setter
    @Builder
    public static class MemberDetail {
        private Long id;
        private String name;
        private String nickname;
        private String expertiseField;
        private List<String> interests; // 가정: interests가 문자열 리스트로 표현됨
        private boolean isLogin;
    }
}
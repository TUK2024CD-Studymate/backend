package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.domain.ChatRoom;
import com.studymate.backend.chat.domain.UserChatRoom;
import com.studymate.backend.chat.dto.ChatMessageRes;
import com.studymate.backend.chat.dto.ChatRoomRes;
import com.studymate.backend.chat.service.ChatService;
import com.studymate.backend.config.security.jwt.TokenProvider;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.domain.UserDetail;
import com.studymate.backend.member.service.CustomUserDetailsService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat", description = "채팅 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("api/chat/rooms")
public class ChatController {
    private final ChatService chatService;
    private final MemberRepository memberRepository;
    private final TokenProvider tokenProvider;

    @Operation(summary = "chatroom create", description = "채팅방 생성")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    @PostMapping
    public ResponseEntity<?> createChatRoom(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No token provided");
        }
        String token = authHeader.substring(7);
        Authentication authentication = tokenProvider.getAuthentication(token);

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Invalid token");
        }

        User principal = (User) authentication.getPrincipal();
        String email = principal.getUsername(); // JWT에서 추출된 사용자 이메일을 가져옵니다.

        // 여기에서 데이터베이스에서 Member를 조회하거나 채팅방 생성 로직을 수행
        // 예시로, Member 객체의 이메일 필드를 사용하여 DB에서 조회
        Member member = memberRepository.findByEmail(email);

        if (chatService.duplicatedUserChatRoom(member)) {
            UserChatRoom userChatRoom = chatService.findUserChatRoomByMemberId(member.getId());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("이미 채팅방이 존재합니다.");
        }

        ChatRoom chatRoom = chatService.createChatRoom();
        chatService.createUserChatRoom(member, chatRoom.getId());
        ChatRoomRes chatRoomRes = ChatRoomRes.builder()
                .chatRoomId(chatRoom.getId())
                .nickname(member.getNickname())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomRes);
    }


    @Operation(summary = "ChatRoomList read", description = "채팅방 목록 조회")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    @GetMapping("/list")
    public ResponseEntity<ChatRoomRes> getChatRoomList () {
        List<ChatRoomRes> chatRoomList = chatService.findChatRoom();

        return ResponseEntity.ok((ChatRoomRes) chatRoomList);
    }
}

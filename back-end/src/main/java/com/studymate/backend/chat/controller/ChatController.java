package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.domain.ChatRoom;
import com.studymate.backend.chat.domain.UserChatRoom;
import com.studymate.backend.chat.dto.ChatMessageRes;
import com.studymate.backend.chat.dto.ChatRoomRes;
import com.studymate.backend.chat.dto.CreateChatRoom;
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
    public ResponseEntity<?> createChatRoom(@RequestHeader("Authorization") String authHeader, @RequestBody CreateChatRoom createChatRoom) {
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
        Member member = memberRepository.findByEmail(email);
        if (member == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Member not found");
        }

        String chatRoomName = createChatRoom.getName();
        if (chatRoomName == null || chatRoomName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Chat room name is required");
        }


        ChatRoom chatRoom = chatService.createChatRoom(chatRoomName); // 서비스 계층에 이름을 전달
        chatService.createUserChatRoom(member, chatRoom.getId());
        ChatRoomRes chatRoomRes = ChatRoomRes.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoomName)// 채팅방 이름 포함
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomRes);
    }


    @Operation(summary = "ChatRoomList read", description = "채팅방 목록 조회")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    @GetMapping("/list")
    public ResponseEntity<List<ChatRoomRes>> getChatRoomList(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7);
        Authentication authentication = tokenProvider.getAuthentication(token);

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        User principal = (User) authentication.getPrincipal();
        String email = principal.getUsername(); // JWT에서 추출된 사용자 이메일을 가져옵니다.
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        List<ChatRoomRes> chatRoomList = chatService.findUserChatRoomByMemberId(member.getId());
        return ResponseEntity.ok(chatRoomList);
    }

}

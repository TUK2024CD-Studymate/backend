package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.domain.ChatRoom;
import com.studymate.backend.chat.dto.ChatMessageRes;
import com.studymate.backend.chat.dto.ChatRoomRes;
import com.studymate.backend.chat.service.ChatService;
import com.studymate.backend.config.security.jwt.TokenProvider;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.transaction.annotation.Transactional;
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
    public ResponseEntity<?> createChatRoom(@RequestHeader("Authorization") String authHeader, @RequestParam String targetNickname) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        String token = authHeader.substring(7); // "Bearer " 제거
        Authentication authentication = tokenProvider.getAuthentication(token);

        if (authentication == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String email = ((User) authentication.getPrincipal()).getUsername();
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        String userNickname = member.getNickname(); // 멤버 객체에서 닉네임 추출
        Member targetMember = memberRepository.findByNickname(targetNickname);

        if (targetMember == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Target member not found");
        }

        Pair<ChatRoom, Boolean> result = chatService.createChatRoom(userNickname, targetNickname);
        ChatRoom chatRoom = result.getFirst();
        boolean isNewRoom = result.getSecond();

        HttpStatus status = isNewRoom ? HttpStatus.CREATED : HttpStatus.OK;
        ChatRoomRes chatRoomRes = ChatRoomRes.builder()
                .chatRoomId(chatRoom.getId())
                .chatRoomName(chatRoom.getName())
                .build();

        return ResponseEntity.status(status).body(chatRoomRes);
    }


    @Transactional
    @GetMapping("/api/chat/rooms/{chatRoomId}/contents")
    public ResponseEntity<?> enterChatRoom(@PathVariable Long chatRoomId) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String email = ((org.springframework.security.core.userdetails.User) authentication.getPrincipal()).getUsername();
        Member member = memberRepository.findByEmail(email);

        if (member == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: Member not found");
        }
        // 채팅방 정보와 메시지 목록 조회
        List<ChatMessageRes> messages = chatService.findChatMessage(chatRoomId, member.getId());

        return ResponseEntity.ok().body(messages);
    }


    @Transactional
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

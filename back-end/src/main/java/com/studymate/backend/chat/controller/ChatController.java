package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.domain.ChatRoom;
import com.studymate.backend.chat.dto.ChatMessageRes;
import com.studymate.backend.chat.dto.ChatRoomRes;
import com.studymate.backend.chat.service.ChatService;
import com.studymate.backend.member.domain.UserDetail;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Chat", description = "채팅 API")
@RestController
@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
@RequestMapping("api/chat/rooms")
public class ChatController {
    private final ChatService chatService;
    @Operation(summary = "chatroom create", description = "채팅방 생성")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    @PostMapping
    public ResponseEntity<?> createChatRoom (@AuthenticationPrincipal UserDetail userDetail) {
//        if (chatService.duplicatedUserChatRoom(userDetail.getMember())) { // 사용자가 이미 채팅방 입장했으면
//            UserChatRoom userChatRoom = chatService.findUserChatRoomByMemberId(userDetail.getMember().getId());
//            return ResponseEntity.ok(ResultResponse.of(ResultCode.USER_CHATROOM_DUPLICATED, userChatRoom.getChatRoom().getId()));
//        }
        if (userDetail == null || userDetail.getMember() == null) {
            // userDetail이 null이면 적절한 응답 반환
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Unauthorized: No user details found");
        }

        ChatRoom chatRoom = chatService.createChatRoom();
        chatService.createUserChatRoom(userDetail.getMember(), chatRoom.getId());
        ChatRoomRes chatRoomRes = ChatRoomRes.builder()
                .chatRoomId(chatRoom.getId())
                .nickname(userDetail.getMember().getNickname())
                .build();

        return ResponseEntity.status(HttpStatus.CREATED).body(chatRoomRes);
    }

    @Operation(summary = "chatmessages read", description = "채팅 내역 조회")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    @GetMapping("{chatRoomId}/messages")
    public ResponseEntity<ChatRoomRes> getChatMessage (@PathVariable Long chatRoomId) {
        // **해당하는 유저만 조회할 수 있도록 추가 작성하기**
        List<ChatMessageRes> chatMessageList = chatService.findChatMessage(chatRoomId);
        return ResponseEntity.ok((ChatRoomRes) chatMessageList);
    }

    @Operation(summary = "ChatRoomList read", description = "채팅방 목록 조회")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    @GetMapping("/list")
    public ResponseEntity<ChatRoomRes> getChatRoomList () {
        List<ChatRoomRes> chatRoomList = chatService.findChatRoom();

        return ResponseEntity.ok((ChatRoomRes) chatRoomList);
    }
}

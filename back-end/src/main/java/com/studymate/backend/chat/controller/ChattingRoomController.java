package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.dto.response.ChattingRoomResponse;
import com.studymate.backend.chat.entity.ChattingRoom;
import com.studymate.backend.chat.service.ChattingRoomService;
import com.studymate.backend.member.domain.Member;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@Tag(name = "Chat", description = "Chatting API")
public class ChattingRoomController {
    private final ChattingRoomService chattingRoomService;

    // 채팅방 생성
    @PostMapping("/chats")
    @Operation(summary = "chattingroom create", description = "채팅방 생성")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    public ResponseEntity<ChattingRoomResponse> createChattingRoom(@AuthenticationPrincipal Member currentUser,
                                                                   @Valid @RequestBody ChattingRoomResponse chattingRoomResponse){
        // 채팅방 생성
        ChattingRoom newChattingRoom = chattingRoomService.createChattingRoom();

        // 현재 사용자를 채팅방에 참여시키기
        chattingRoomService.createChatParticipation(currentUser, newChattingRoom.getId());

        // DTO를 응답으로 반환
        return ResponseEntity.status(HttpStatus.CREATED).body(chattingRoomResponse);
    }


    // 특정 사용자의 채팅방 내역 조회
    @GetMapping("/chats/user/{userId}")
    public ResponseEntity<List<ChattingRoomResponse>> findChattingRoomsByUserId(@PathVariable Long userId) {
        List<ChattingRoomResponse> chattingRooms = chattingRoomService.findChattingRoomsByUserId(userId);
        return ResponseEntity.ok(chattingRooms);
    }

    // 채팅방 삭제
    @DeleteMapping("/chats/{chattingRoomId}")
    public ResponseEntity<String> deleteChattingRoom(@PathVariable Long chattingRoomId) {
        String result = chattingRoomService.deleteChattingRoom(chattingRoomId);
        return ResponseEntity.ok(result);
    }
}

package com.studymate.backend.chat.controller;

import com.studymate.backend.chat.domain.ChatRoom;
import com.studymate.backend.chat.repository.ChatRoomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@Controller
@RequestMapping("/api/chat")
@Tag(name = "채팅", description = "채팅 API")
public class ChatRoomController {

    private final ChatRoomRepository chatRoomRepository;

    @GetMapping("/room")
    public String rooms(Model model) {
        return "/chat/room";
    }

    @GetMapping("/rooms")
    @Operation(summary = "채팅방 리스트 조회", description = "회원이 채팅방리스트를 조회한다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    @ResponseBody
    public List<ChatRoom> room() {
        return chatRoomRepository.findAllRoom();
    }

    @PostMapping("/room")
    @Operation(summary = "채팅방 생성", description = "회원이 채팅방을 생성한다")
    @ApiResponses(value = @ApiResponse(responseCode = "201", description = "성공"))
    @ResponseBody
    public ChatRoom createRoom(@RequestParam String name) {
        return chatRoomRepository.createChatRoom(name);
    }

    @GetMapping("/room/enter/{roomId}")
    public String roomDetail(Model model, @PathVariable Integer roomId) {
        model.addAttribute("roomId", roomId);
        return "/chat/roomdetail";
    }


    @GetMapping("/room/{roomId}")
    @Operation(summary = "채팅방 조회", description = "회원이 채팅방을 조회한다")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    @ResponseBody
    public ChatRoom roomInfo(@PathVariable Integer roomId) {
        return chatRoomRepository.findRoomById(roomId);
    }
}
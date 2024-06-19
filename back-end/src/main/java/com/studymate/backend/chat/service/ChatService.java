package com.studymate.backend.chat.service;

import com.studymate.backend.chat.domain.ChatMessage;
import com.studymate.backend.chat.domain.ChatRoom;
import com.studymate.backend.chat.domain.UserChatRoom;
import com.studymate.backend.chat.dto.ChatMessageRes;
import com.studymate.backend.chat.dto.ChatRoomRes;
import com.studymate.backend.chat.dto.CreateMessageReq;
import com.studymate.backend.chat.mapper.ChatMapper;
import com.studymate.backend.chat.repository.ChatMessageRepository;
import com.studymate.backend.chat.repository.ChatRoomRepository;
import com.studymate.backend.chat.repository.UserChatRoomRepository;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class ChatService {

    private final UserChatRoomRepository userChatRoomRepository;
    private final ChatRoomRepository chatRoomRepository;
    private final ChatMessageRepository chatMessageRepository;
    private final ChatMapper chatMapper;
    private final MemberRepository memberRepository;
    private final MessageService messageService;


    @Transactional
    public Pair<ChatRoom, Boolean> createChatRoom(String userNickname, String targetNickname) {

        // 자기 자신에게 채팅 요청하는 경우 예외 처리
        if (userNickname.equals(targetNickname)) {
            throw new IllegalArgumentException("자신과 채팅방을 생성할 수 없습니다.");
        }

        String[] nicknames = {userNickname, targetNickname};
        Arrays.sort(nicknames);
        String chatRoomName = String.format("%s & %s", nicknames[0], nicknames[1]);
        Optional<ChatRoom> existingRoom = chatRoomRepository.findByName(chatRoomName);
        if (existingRoom.isPresent()) {
            return Pair.of(existingRoom.get(), false); // 이미 존재하는 채팅방 반환
        }

        ChatRoom newChatRoom = ChatRoom.builder()
                .name(chatRoomName)
                .build();
        chatRoomRepository.save(newChatRoom);

        // 채팅방에 멤버 추가
        addMemberToRoom(newChatRoom, userNickname);
        addMemberToRoom(newChatRoom, targetNickname);

        return Pair.of(newChatRoom, true); // 새로 생성된 채팅방 반환
    }

    private void addMemberToRoom(ChatRoom chatRoom, String nickname) {
        Member member = memberRepository.findByNickname(nickname);
        if (member != null) {
            UserChatRoom newUserChatRoom = new UserChatRoom();
            newUserChatRoom.setMember(member);
            newUserChatRoom.setChatRoom(chatRoom);
            userChatRoomRepository.save(newUserChatRoom);
        }
    }
    @Transactional
    public List<ChatRoomRes> findUserChatRoomByMemberId(Long memberId) {
        List<UserChatRoom> userChatRooms = userChatRoomRepository.findByMemberId(memberId);
        List<ChatRoomRes> chatRoomResList = new ArrayList<>();

        for (UserChatRoom userChatRoom : userChatRooms) {
            ChatRoom chatRoom = userChatRoom.getChatRoom();
            // 모든 멤버 조회
            List<Member> allMembers = userChatRoomRepository.findByChatRoomId(chatRoom.getId())
                    .stream()
                    .map(UserChatRoom::getMember)
                    .collect(Collectors.toList());
            // 현재 사용자를 제외한 멤버 필터링
            List<Member> otherMembers = allMembers.stream()
                    .filter(m -> !m.getId().equals(memberId))
                    .collect(Collectors.toList());
            Long unreadMessageCount = countUnreadMessages(chatRoom.getId(), memberId); // 읽지 않은 메시지 수 계산

            ChatRoomRes chatRoomRes = chatMapper.toChatRoomDto(chatRoom, otherMembers, unreadMessageCount); // 수정된 메소드 호출
            chatRoomResList.add(chatRoomRes);
        }
        return chatRoomResList;
    }


    @Transactional
    public void saveMessage(Member sender, Long chatRoomId, CreateMessageReq createMessageReq) {
        ChatRoom chatRoom = chatRoomRepository.findById(chatRoomId)
                .orElseThrow(() -> new IllegalArgumentException("채팅룸 아이디에 해당하는 채팅룸이 존재하지 않습니다: " + chatRoomId));
        ChatMessage message = chatMapper.toChatMessage(sender, chatRoom, createMessageReq);

        chatMessageRepository.save(message);
        log.info("메시지가 저장됐습니다.");
    }

    public Long countUnreadMessages(Long chatRoomId, Long memberId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId);
        String lastReadMessageId = messageService.getUserLastReadPosition(chatRoomId.toString(), memberId.toString());
        long lastReadId = lastReadMessageId != null ? Long.parseLong(lastReadMessageId) : 0;

        return chatMessages.stream()
                .filter(msg -> msg.getId() > lastReadId && !msg.getMember().getId().equals(memberId))
                .count();
    }


    @Transactional
    public List<ChatMessageRes> findChatMessage(Long chatRoomId, Long memberId) {
        List<ChatMessage> chatMessages = chatMessageRepository.findByChatRoomId(chatRoomId);
        String lastMessageId = getLastMessageId(chatMessages);
        messageService.updateUserReadPosition(chatRoomId.toString(), memberId.toString(), lastMessageId, false); // 'false'로 설정하여 메시지 읽음 상태 변경을 허용하지 않음
        // 업데이트를 기다리지 않고 캐시된 최신 읽음 위치를 바로 사용
        String lastReadMessageId = messageService.getUserLastReadPosition(chatRoomId.toString(), memberId.toString());
        return chatMapper.toChatMessageList(chatMessages, lastReadMessageId);
    }

    private String getLastMessageId(List<ChatMessage> chatMessages) {
        if (!chatMessages.isEmpty()) {
            return String.valueOf(chatMessages.get(chatMessages.size() - 1).getId());
        }
        return "0"; // 빈 목록일 경우 "0" 반환
    }


}

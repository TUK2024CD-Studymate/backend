package com.studymate.backend.chat.service;

import com.studymate.backend.chat.domain.ChatMessage;
import com.studymate.backend.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service("chatMessageService")
public class MessageService {
    private final RedisTemplate<String, String> redisTemplate;
    private final ChatMessageRepository messageRepository;

    @Autowired
    public MessageService(RedisTemplate<String, String> redisTemplate, ChatMessageRepository messageRepository) {
        this.redisTemplate = redisTemplate;
        this.messageRepository = messageRepository;
    }

    @Transactional
    public void updateUserReadPosition(String roomId, String userId, String lastMessageId) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        hashOperations.put("userLastRead:" + roomId, userId, lastMessageId);

        // 데이터베이스에서 해당 메시지 객체를 가져와 읽음 상태를 업데이트합니다.
        long lastMsgId = Long.parseLong(lastMessageId);
        ChatMessage message = messageRepository.findById(lastMsgId).orElse(null);
        if (message != null) {
            message.setRead(true);
            messageRepository.save(message);
        }
    }

    @Transactional
    public String getUserLastReadPosition(String roomId, String userId) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get("userLastRead:" + roomId, userId);
    }
}

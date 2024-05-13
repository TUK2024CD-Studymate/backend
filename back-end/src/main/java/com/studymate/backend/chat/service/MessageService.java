package com.studymate.backend.chat.service;

import com.studymate.backend.chat.domain.ChatMessage;
import com.studymate.backend.chat.repository.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.HashOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.TimeUnit;

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

        long lastMsgId = Long.parseLong(lastMessageId);
        ChatMessage message = messageRepository.findById(lastMsgId).orElse(null);
        if (message != null) {
            message.setRead(true);
            messageRepository.saveAndFlush(message); // 데이터베이스에 즉시 반영
        }
        // 캐시 갱신
        redisTemplate.expire("userLastRead:" + roomId, 10, TimeUnit.MINUTES);
    }

    public String getUserLastReadPosition(String roomId, String userId) {
        HashOperations<String, String, String> hashOperations = redisTemplate.opsForHash();
        return hashOperations.get("userLastRead:" + roomId, userId);
    }
}

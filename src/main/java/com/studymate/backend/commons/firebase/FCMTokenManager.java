package com.studymate.backend.commons.firebase;

import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FCMTokenManager {
    private static final String FIREBASE_CONFIG_PATH = "firebase/studymate-92070-firebase-adminsdk-t4116-5c71b9db48.json";

    @Resource
    private RedisTemplate<String, String> redisTemplate;


    // 레디스에 토큰 저장
    private void set(String key, String value) {
        redisTemplate.opsForValue().set(key, value);
    }

    // 레디스에서 userId를 통해 토큰 조회
    private String get(String key) {
        return redisTemplate.opsForValue().get(key);
    }

    private void del(String key) {
        redisTemplate.delete(key);
    }

    public void saveToken(String userId, String token) {
        set(userId, token);
    }

    public String getToken(String userId) {
        return get(userId);
    }

    public void deleteToken(String userId) {
        del(userId);
    }

    @Async
    public void deleteAndSaveFCMToken(String userId, String token) {
        del(userId);
        set(userId,token);
    }
}

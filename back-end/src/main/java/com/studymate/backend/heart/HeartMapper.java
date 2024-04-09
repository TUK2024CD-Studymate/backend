package com.studymate.backend.heart;

import com.studymate.backend.heart.domain.Heart;
import com.studymate.backend.heart.dto.HeartResponse;
import org.springframework.stereotype.Component;

@Component
public class HeartMapper {
    public HeartResponse toResponse(Heart heart) {
        return HeartResponse.builder()
                .postId(heart.getPost().getId())
                .nickname(heart.getMember().getNickname())
                .createAt(heart.getCreatedAt())
                .build();
    }
}

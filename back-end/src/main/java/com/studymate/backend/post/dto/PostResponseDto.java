package com.studymate.backend.post.dto;

import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.post.domain.Post;
import lombok.Getter;

import java.time.format.DateTimeFormatter;

@Getter
public class PostResponseDto {
    private Long id;
    private String content;
    private String title;
    private ProfileImg profileUrl;
    private String createdAt;

    private String nickname;

    public PostResponseDto(Post entity){
        this.id = entity.getId();
        this.nickname = entity.getMember().getNickname();
        this.title = entity.getTitle();
        this.content = entity.getContent();
        this.profileUrl = entity.getMember().getProfileUrl();
        this.createdAt = entity.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy.MM.dd"));

    }


}

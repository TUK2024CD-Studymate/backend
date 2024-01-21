package com.studymate.backend.post.dto;
import com.studymate.backend.post.domain.entity.Post;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PostDto {
    private long id;
    private String title;
    private String content;
    private String writer;

    public static PostDto toDto(Post post){
        return new PostDto(
                post.getId(),
                post.getTitle(),
                post.getContent(),
                post.getMember().getName());
    }
}

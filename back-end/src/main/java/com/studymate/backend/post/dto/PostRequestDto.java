package com.studymate.backend.post.dto;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.post.domain.Post;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter
public class PostRequestDto {
    private String title;
    private String content;
    private Long memberId;
    private String category;

    public PostRequestDto(String title, String content, Long memberId, String category){
        this.title = title;
        this.content = content;
        this.memberId = memberId;
        this.category = category;
    }

    public Post toEntity(Member member){
        return Post.builder()
                .title(title)
                .content(content)
                .member(member)
                .category(category)
                .build();
    }
}

package com.studymate.backend.post.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "post_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id",updatable = false)
    private Member member;

    @Column(length = 30)
    private String title;

    @Column(length = 500)
    private String content;

    private String category;

    public void update(String title, String content, String category){
        this.title = title;
        this.content = content;
        this.category = category;
    }

}

package com.studymate.backend.comment.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.post.domain.Post;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Comment extends BaseTimeEntity {
    @Id
    @GeneratedValue
    @Column(name = "comment_id")
    private Long id;

    @ManyToOne
    @JoinColumn(name="user_id",updatable = false)
    private Member member;

    @ManyToOne
    @JoinColumn(name="post_id", updatable = false)
    private Post post;

    @Column(length = 100)
    private String content;

    public void update(String content){
        this.content = content;
    }



}

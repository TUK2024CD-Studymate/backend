package com.studymate.backend.post.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.heart.domain.Heart;
import com.studymate.backend.member.domain.Category;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Getter
@Entity
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@ToString
public class Post extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // MySQL에 맞는 전략 명시
    @Column(name = "post_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name="user_id",updatable = false)
    private Member member;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Heart> heart;

    @Column(length = 30)
    private String title;

    @Column(length = 500)
    private String content;

    private Category category;

    @Enumerated(EnumType.STRING)
    private Interests interests;

    private Integer likeCount;

    private Boolean recruitmentStatus;

    public void update(String title, String content, Category category, Interests interests, Boolean recruitmentStatus){
        this.title = title;
        this.content = content;
        this.category = category;
        this.interests = interests;
        if (category == Category.STUDY) {
            this.recruitmentStatus = recruitmentStatus;
        }
    }
}


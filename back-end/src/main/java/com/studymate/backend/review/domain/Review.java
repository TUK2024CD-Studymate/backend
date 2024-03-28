package com.studymate.backend.review.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.review.dto.ReviewUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Long id;
    private String title;
    private String content;
    private String writer;
    private String mentor;
    private int star;
    private Boolean isSolved;
    private Boolean heart;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    public void confirmWriter(Member member) {
        this.member = member;
        this.writer = member.getNickname();
    }
    public void confirmMentor(Member member) {
        this.mentor = member.getNickname();
    }

    public void setSolved(boolean isSolved) {
        this.isSolved = isSolved;
    }
    public void setHeart(boolean heart) {
        this.heart = heart;
    }

    public void update(ReviewUpdateRequest request) {
        this.heart = request.getHeart();
        this.title = request.getTitle();
        this.content = request.getContent();
        this.isSolved = request.getIsSolved();
        this.star = request.getStar();
    }
}

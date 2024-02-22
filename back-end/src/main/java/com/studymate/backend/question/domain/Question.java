package com.studymate.backend.question.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Question extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "question_id")
    private Long id;
    private String writer;
    private String title;
    private String content;
    @Enumerated(value = EnumType.STRING)
    private Interests interests;
    @ColumnDefault("FALSE")
    private Boolean isSolved;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;
    public void confirmWriter(Member member) {
        this.writer = member.getNickname();
    }

    public void confirmMember(Member member) {
        this.member = member;
    }

    public void solved() {
        this.isSolved = true;
    }
}

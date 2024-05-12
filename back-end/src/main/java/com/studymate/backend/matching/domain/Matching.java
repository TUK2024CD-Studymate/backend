package com.studymate.backend.matching.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.question.domain.Question;
import jakarta.persistence.*;
import lombok.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
@Getter
public class Matching extends BaseTimeEntity {

    @Id
    @Column(name = "matching_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private Long senderId;  // 매칭 신청자 id값

    private Long receiverId;  // 매칭 신청을 받는 사람의 id값

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Member member;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    public void confirmMember(Member member) {
        this.member = member;
    }

    public void confirmSenderId(Member member) {
        this.senderId = member.getId();
    }
    public void confirmReceiverId(Member member) {
        this.receiverId = member.getId();
    }

    public void confirmQuestion(Question question) {
        this.question = question;
    }
}

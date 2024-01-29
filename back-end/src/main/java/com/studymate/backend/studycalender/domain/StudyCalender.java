package com.studymate.backend.studycalender.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Builder
@Getter
@AllArgsConstructor
@Table(name = "study_calender")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudyCalender extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "calender_id")
    private Long id;

    private String content;

    @Enumerated(value = EnumType.STRING)
    private Interests studyClass;

    private LocalDateTime startTime;

    private LocalDateTime endTime;

    @ManyToOne
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public void update(String content, Interests studyClass,
                       LocalDateTime startTime, LocalDateTime endTime) {
        this.content = content;
        this.studyClass = studyClass;
        this.startTime = startTime;
        this.endTime = endTime;
    }
}

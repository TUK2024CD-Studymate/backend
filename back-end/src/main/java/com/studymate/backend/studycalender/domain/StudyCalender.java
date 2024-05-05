package com.studymate.backend.studycalender.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.LocalTime;

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
    private String studyClass;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private LocalTime entireTime;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Member member;

    public void update( String studyClass, LocalDateTime startTime, LocalDateTime endTime) {
        this.studyClass = studyClass;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    public long convertSeconds(LocalDateTime startTime, LocalDateTime endTime) {
        Duration time = Duration.between(startTime, endTime);
        return time.toSeconds();
    }

    public void setEntireTime(long time) {
        Duration duration = Duration.ofSeconds(time);
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();
        this.entireTime = LocalTime.of((int) hours, minutes,seconds);
    }

    public String serializeTime(LocalTime time) {
        return time.toString();
    }
}

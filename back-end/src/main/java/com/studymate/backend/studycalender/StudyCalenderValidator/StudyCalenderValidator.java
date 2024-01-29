package com.studymate.backend.studycalender.StudyCalenderValidator;

import com.studymate.backend.studycalender.domain.StudyCalender;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class StudyCalenderValidator {

    public void timeValidator(LocalDateTime startTime, LocalDateTime endTime) {

        if (startTime.isAfter(LocalDateTime.now())) {
            throw new RuntimeException("스터디 시작 일시를 제대로 입력하세요.");
        }
        if (endTime.isAfter(LocalDateTime.now())) {
            throw new RuntimeException("스터디 종료 일시를 제대로 입력하세요.");
        }
        if (endTime.isBefore(startTime)) {
            throw new RuntimeException("스터디 종료 일시가 시작 일시보다 빠릅니다.");
        }
    }

    public void calenderExistValidator(StudyCalender calender) {
        if (calender == null) {
            throw new RuntimeException("캘린더가 존재하지 않습니다.");
        }
    }
}

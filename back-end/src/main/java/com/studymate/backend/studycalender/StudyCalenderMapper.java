package com.studymate.backend.studycalender;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.studycalender.domain.StudyCalender;
import com.studymate.backend.studycalender.dto.CalenderCreateRequest;
import com.studymate.backend.studycalender.dto.CalenderListResponse;
import com.studymate.backend.studycalender.dto.CalenderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class StudyCalenderMapper {

    public StudyCalender toEntity(CalenderCreateRequest request, Member member) {

        StudyCalender calender = StudyCalender.builder()
                .content(request.getContent())
                .endTime(request.getEndTime())
                .studyClass(request.getStudyClass())
                .startTime(request.getStartTime())
                .member(member)
                .build();
        return calender;
    }

    public CalenderResponse toResponse(StudyCalender studyCalender) {

        CalenderResponse calender = CalenderResponse.builder()
                .content(studyCalender.getContent())
                .endTime(studyCalender.getEndTime())
                .studyClass(studyCalender.getStudyClass())
                .startTime(studyCalender.getStartTime())
                .build();

        return calender;
    }

    public CalenderListResponse toListResponse(List<StudyCalender> calenderList) {
        List<CalenderResponse> calenderResponseList =
                calenderList.stream().map(this::toResponse).collect(Collectors.toList());

        return CalenderListResponse.builder()
                .calenderList(calenderResponseList)
                .build();
    }
}

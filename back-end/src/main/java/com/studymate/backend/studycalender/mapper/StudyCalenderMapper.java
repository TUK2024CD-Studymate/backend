package com.studymate.backend.studycalender.mapper;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.studycalender.domain.CalenderSubject;
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

    public StudyCalender toEntity(CalenderCreateRequest request, Member member, CalenderSubject subject) {

        return StudyCalender.builder()
                .endTime(request.getEndTime())
                .startTime(request.getStartTime())
                .subjectName(subject.getSubjectName())
                .member(member)
                .build();
    }

    public CalenderResponse toResponse(StudyCalender studyCalender) {

        return CalenderResponse.builder()
                .id(studyCalender.getId())
                .endTime(studyCalender.getEndTime())
                .startTime(studyCalender.getStartTime())
                .subjectName(studyCalender.getSubjectName())
                .entireTime(studyCalender.serializeTime(studyCalender.getEntireTime()))
                .build();
    }

    public CalenderListResponse toListResponse(List<StudyCalender> calenderList) {
        List<CalenderResponse> calenderResponseList =
                calenderList.stream().map(this::toResponse).collect(Collectors.toList());

        return CalenderListResponse.builder()
                .calenderList(calenderResponseList)
                .build();
    }
}

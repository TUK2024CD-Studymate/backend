package com.studymate.backend.studycalender.mapper;

import com.studymate.backend.studycalender.domain.CalenderSubject;
import com.studymate.backend.studycalender.domain.StudyCalender;
import com.studymate.backend.studycalender.dto.CalenderListResponse;
import com.studymate.backend.studycalender.dto.CalenderResponse;
import com.studymate.backend.studycalender.dto.SubjectCreateRequest;
import com.studymate.backend.studycalender.dto.SubjectResponse;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CalenderSubjectMapper {

    public CalenderSubject toEntity(SubjectCreateRequest request) {

        return CalenderSubject.builder()
                .subjectName(request.getSubjectName())
                .build();
    }

    public SubjectResponse toResponse(CalenderSubject calenderSubject) {

        return SubjectResponse.builder()
                .id(calenderSubject.getId())
                .subjectName(calenderSubject.getSubjectName())
                .build();
    }

    public CalenderListResponse toListResponse(List<CalenderSubject> calenderSubjects) {
        List<CalenderResponse> calenderResponseList =
                calenderList.stream().map(this::toResponse).collect(Collectors.toList());

        return CalenderListResponse.builder()
                .calenderList(calenderResponseList)
                .build();
    }
}

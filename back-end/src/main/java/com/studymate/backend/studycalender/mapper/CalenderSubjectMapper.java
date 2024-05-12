package com.studymate.backend.studycalender.mapper;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.studycalender.domain.CalenderSubject;
import com.studymate.backend.studycalender.dto.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CalenderSubjectMapper {

    public CalenderSubject toEntity(SubjectCreateRequest request, Member member) {

        return CalenderSubject.builder()
                .member(member)
                .subjectName(request.getSubjectName())
                .build();
    }

    public SubjectResponse toResponse(CalenderSubject calenderSubject) {

        return SubjectResponse.builder()
                .id(calenderSubject.getId())
                .subjectName(calenderSubject.getSubjectName())
                .build();
    }

    public SubjectListResponse toListResponse(List<CalenderSubject> calenderSubjects) {
        List<SubjectResponse> subjectResponses = calenderSubjects.
                stream().map(this::toResponse).collect(Collectors.toList());

        return SubjectListResponse.builder()
                .subjectList(subjectResponses)
                .build();
    }
}

package com.studymate.backend.studycalender.service;


import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.studycalender.domain.CalenderSubject;
import com.studymate.backend.studycalender.dto.SubjectCreateRequest;
import com.studymate.backend.studycalender.dto.SubjectListResponse;
import com.studymate.backend.studycalender.dto.SubjectResponse;
import com.studymate.backend.studycalender.dto.SubjectUpdateRequest;
import com.studymate.backend.studycalender.mapper.CalenderSubjectMapper;
import com.studymate.backend.studycalender.repository.CalenderSubjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CalenderSubjectService {
    private final MemberService memberService;
    private final CalenderSubjectRepository calenderSubjectRepository;
    public final CalenderSubjectMapper calenderSubjectMapper;

    @Transactional
    public SubjectResponse createSubject(SubjectCreateRequest request) {
        Member member = memberService.getMember();
        CalenderSubject calenderSubject = calenderSubjectMapper.toEntity(request,member);

        SubjectResponse response = calenderSubjectMapper.toResponse(calenderSubjectRepository.save(calenderSubject));
        return response;
    }

    public SubjectResponse findOne(Long id) {
        CalenderSubject subject = calenderSubjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found subject"));

        return calenderSubjectMapper.toResponse(subject);
    }

    @Transactional
    public SubjectListResponse findAll() {
        Member member = memberService.getMember();

        List<CalenderSubject> subjects = calenderSubjectRepository.findAllByMember(member);
        return calenderSubjectMapper.toListResponse(subjects);
    }

    @Transactional
    public SubjectResponse update(SubjectUpdateRequest request, Long id) {
        memberService.getMember();
        CalenderSubject subject = calenderSubjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found subject"));

        subject.update(request);

        return calenderSubjectMapper.toResponse(subject);
    }

    public String delete(Long id) {
        CalenderSubject calenderSubject = calenderSubjectRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found subject"));

        calenderSubjectRepository.delete(calenderSubject);
        return "정상적으로 삭제되었습니다.";
    }
}

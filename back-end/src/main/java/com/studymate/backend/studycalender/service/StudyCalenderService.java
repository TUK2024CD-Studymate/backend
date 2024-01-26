package com.studymate.backend.studycalender.service;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.studycalender.StudyCalenderMapper;
import com.studymate.backend.studycalender.StudyCalenderRepository;
import com.studymate.backend.studycalender.StudyCalenderValidator.StudyCalenderValidator;
import com.studymate.backend.studycalender.domain.StudyCalender;
import com.studymate.backend.studycalender.dto.CalenderCreateRequest;
import com.studymate.backend.studycalender.dto.CalenderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class StudyCalenderService {

    private final StudyCalenderMapper studyCalenderMapper;
    private final StudyCalenderRepository studyCalenderRepository;

    private final MemberService memberService;
    private final StudyCalenderValidator validator;

    @Transactional
    public String createPost(CalenderCreateRequest request) {

        Member member = memberService.getMember();

        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();

        validator.timeValidator(startTime, endTime);

        StudyCalender calender = studyCalenderMapper.toEntity(request, member);

        studyCalenderRepository.save(calender);

        return "스터디기록이 생성되었습니다.";
    }

    public CalenderResponse findOne(Long id) {

        Member member = memberService.getMember();

        StudyCalender calender = studyCalenderRepository.findByMemberAndId(member, id);

        if (calender == null) {
            throw new RuntimeException("기록을 찾지 못했습니다.");
        }
        CalenderResponse calenderResponse = studyCalenderMapper.toResponse(calender);

        return calenderResponse;
    }
}

package com.studymate.backend.studycalender.service;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.studycalender.StudyCalenderMapper;
import com.studymate.backend.studycalender.StudyCalenderRepository;
import com.studymate.backend.studycalender.StudyCalenderValidator.StudyCalenderValidator;
import com.studymate.backend.studycalender.domain.StudyCalender;
import com.studymate.backend.studycalender.dto.CalenderCreateRequest;
import com.studymate.backend.studycalender.dto.CalenderListResponse;
import com.studymate.backend.studycalender.dto.CalenderResponse;
import com.studymate.backend.studycalender.dto.CalenderUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

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

        validator.calenderExistValidator(calender);

        return studyCalenderMapper.toResponse(calender);
    }

    @Transactional
    public CalenderResponse update(Long id, CalenderUpdateRequest request) {

        LocalDateTime startTime = request.getStartTime();
        LocalDateTime endTime = request.getEndTime();

        validator.timeValidator(startTime, endTime);

        Member member = memberService.getMember();
        StudyCalender calender = studyCalenderRepository.findByMemberAndId(member, id);

        validator.calenderExistValidator(calender);

        calender.update(request.getContent(), request.getInterests(),
                request.getStartTime(), request.getEndTime());

        return studyCalenderMapper.toResponse(calender);
    }

    public String delete(Long id) {
        Member member = memberService.getMember();
        StudyCalender calender = studyCalenderRepository.findByMemberAndId(member, id);

        validator.calenderExistValidator(calender);

        studyCalenderRepository.delete(calender);
        return "정상적으로 삭제되었습니다.";
    }

    public CalenderListResponse findAll() {
        Member member = memberService.getMember();
        List<StudyCalender> calenderList = studyCalenderRepository.findAllByMember(member);

        return studyCalenderMapper.toListResponse(calenderList);
    }
}

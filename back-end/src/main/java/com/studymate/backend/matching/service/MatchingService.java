package com.studymate.backend.matching.service;

import com.studymate.backend.commons.firebase.PushNotificationService;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.domain.Part;
import com.studymate.backend.member.dto.MemberListResponse;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.question.QuestionRepository;
import com.studymate.backend.question.domain.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingService {
    private final MemberService memberService;
    private final QuestionRepository questionRepository;
    private final PushNotificationService pushNotificationService;

    public MemberListResponse getMentorList(Long questionId) {
        Member member = memberService.getMember();
        Question question = questionRepository.
                findById(questionId).orElseThrow(() -> new RuntimeException("not found question id"));

        Interests interests = question.getInterests();

        MemberListResponse mentorList = memberService.findMentorByInterest(interests);

        /*
         * 만약 조회하는 사용자의 PART가 멘토라면 자신의 정보를 빼고 조회한다.
         */

        if (member.getPart() == Part.MENTOR) {
            mentorList.getMemberList().removeIf(m -> m.getId().equals(member.getId()));
        }

        return mentorList;
    }

    public String matching(Long questionId, Long mentorId) {

        log.info("FCM start");
        pushNotificationService.matchingNotification(mentorId, questionId);
        log.info("FCM finish");

        return "해당 멘토에게 매칭 알림을 보냈습니다.";
    }
}

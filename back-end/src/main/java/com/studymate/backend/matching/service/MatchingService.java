package com.studymate.backend.matching.service;

import com.studymate.backend.commons.firebase.PushNotificationService;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.domain.Part;
import com.studymate.backend.member.dto.MemberListResponse;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.question.QuestionRepository;
import com.studymate.backend.question.domain.Question;
import com.studymate.backend.review.ReviewMapper;
import com.studymate.backend.review.ReviewRepository;
import com.studymate.backend.review.domain.Review;
import com.studymate.backend.review.dto.ReviewResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingService {
    private final MemberService memberService;
    private final QuestionRepository questionRepository;
    private final PushNotificationService pushNotificationService;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

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

    public String matchingForSms(Long questionId, Long mentorId) {

        Message coolsms = new Message(apiKey, apiSecret);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("not found question"));

        Member mentor = memberRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("not found mentor"));

        String to = mentor.getTel();

        HashMap<String, String> message = makeParams(to, question, mentor);

        try {
            JSONObject obj = (JSONObject) coolsms.send(message);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }


        return mentor.getNickname()+"님에게 전송문자 전송을 하였습니다.";

    }

    private HashMap<String, String> makeParams(String to, Question question, Member mentor) {
        HashMap<String, String> params = new HashMap<>();
        params.put("from", fromNumber);
        params.put("type", "LMS");
        params.put("app_version", "test app 1.2");
        params.put("to", to);
        params.put("text", "[StudyMate]\n"+question.getWriter()+"님께서 "+mentor.getNickname()+"님에게 도움을 요청했어요!\n\n" +
                "[내용제목] : "+question.getTitle()+"\n[내용상세] :" +question.getContent()+ "\n" +
                "[채팅 참여링크] : http://studymate-tuk.kro.kr/chat");
        return params;
    }

    public List<ReviewResponse> searchMentorReview(Long mentorId) {
        Member mentor = memberRepository.findById(mentorId)
                .orElseThrow(() -> new RuntimeException("not found mentor id"));

        List<Review> reviews = reviewRepository.findAllByMentor(mentor.getNickname());

        List<ReviewResponse> reviewResponses = reviews.stream().map(reviewMapper::toResponse)
                .toList();

        return reviewResponses;
    }
}

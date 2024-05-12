package com.studymate.backend.commons.firebase;

import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.question.QuestionRepository;
import com.studymate.backend.question.domain.Question;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class PushNotificationService {

    private final FCMTokenManager fcmTokenManager;
    private final MemberRepository memberRepository;
    private final QuestionRepository questionRepository;
    private final FCMService fcmService;

    // 해당 멘토에게 매칭을 신청할시 알림을 전송한다.
    @Async
    public void matchingNotification(Long mentorId, Long questionId) {
        log.info("PushNotificationService matchingNotification");
        Optional<Member> targetMentor = memberRepository.findById(mentorId);
        Optional<Question> sendQuestion = questionRepository.findById(questionId);


        // 필요한거: 매칭 신청자 닉네임, 멘토 닉네임, 질문 제목, 질문 내용, 멘토 FCM토큰
        if (targetMentor.isPresent() && sendQuestion.isPresent()) {
            String title = sendQuestion.get().getTitle();  // 질문 제목
            String content = sendQuestion.get().getContent();  // 질문 내용
            String writer = sendQuestion.get().getWriter();  // 매칭 신청자 닉네임
            String nickname = targetMentor.get().getNickname();  // 멘토 닉네임
            String targetMentorToken = fcmTokenManager.getToken(String.valueOf(mentorId));  // 멘토의 FCM토큰

            Optional.ofNullable(targetMentorToken).ifPresent(token -> fcmService.sendMessage(
                    token,
                    "매칭을 신청했습니다!",
                    writer + "님이" + nickname + "님에게 매칭을 신청했습니다!" + "\n"
                            + "질문 제목: " + title + "질문 내용: " + content)
            );
        }
    }
}

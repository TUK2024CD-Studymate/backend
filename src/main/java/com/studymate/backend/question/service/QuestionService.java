package com.studymate.backend.question.service;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.question.QuestionRepository;
import com.studymate.backend.question.domain.Question;
import com.studymate.backend.question.dto.QuestionCreateRequest;
import com.studymate.backend.question.dto.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {

    private final QuestionRepository questionRepository;
    private final MemberService memberService;

    public QuestionResponse create(QuestionCreateRequest request) {
        Member member = memberService.getMember();
        Question question = request.toEntity(request);

        question.confirmWriter(member);
        question.confirmMember(member);

        questionRepository.save(question);
        return new QuestionResponse().toResponse(question);
    }

    public QuestionResponse getQuestion(Long id) {
        Member member = memberService.getMember();
        Question question = questionRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("not found Question id"));

        return new QuestionResponse().toResponse(question);
    }

    public List<QuestionResponse> getQuestions() {
        Member member = memberService.getMember();
        List<Question> questionList = questionRepository.findAllByMember(member);
        return new QuestionResponse().toList(questionList);
    }
}

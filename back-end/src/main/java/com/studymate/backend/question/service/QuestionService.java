package com.studymate.backend.question.service;

import com.studymate.backend.global.gpt.dto.GPTRequest;
import com.studymate.backend.global.gpt.dto.GPTResponse;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.question.QuestionRepository;
import com.studymate.backend.question.domain.Question;
import com.studymate.backend.question.dto.QuestionAiRequest;
import com.studymate.backend.question.dto.QuestionAiResponse;
import com.studymate.backend.question.dto.QuestionCreateRequest;
import com.studymate.backend.question.dto.QuestionResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
@RequiredArgsConstructor
public class QuestionService {
    private final QuestionRepository questionRepository;
    private final MemberService memberService;
    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.url}")
    private String apiURL;
    private final RestTemplate restTemplate;
    private static final String MESSAGE = " 앞의 문장을 맞춤법을 검사하고 자연스럽게 해서 반환해줘.";

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

    public QuestionAiResponse createQuestionByAi(QuestionAiRequest request) {
        String questionMessage = request.getQuestionMessage();

        GPTRequest gptRequest = new GPTRequest(model, questionMessage+MESSAGE, 1, 256, 1, 2, 2);
        GPTResponse response = restTemplate.postForObject(apiURL, gptRequest, GPTResponse.class);

        return QuestionAiResponse.builder()
                .questionMessage(response.getChoices().get(0).getMessage().getContent())
                .build();
    }
}
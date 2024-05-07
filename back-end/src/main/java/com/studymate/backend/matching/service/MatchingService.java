package com.studymate.backend.matching.service;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studymate.backend.chat.service.ChatService;
import com.studymate.backend.commons.firebase.PushNotificationService;
import com.studymate.backend.global.gpt.dto.GPTRequest;
import com.studymate.backend.global.gpt.dto.GPTResponse;
import com.studymate.backend.matching.dto.JsonMentorResponse;
import com.studymate.backend.matching.dto.MatchingSseResponse;
import com.studymate.backend.member.MemberMapper;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Interests;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.domain.Part;
import com.studymate.backend.member.dto.MemberListResponse;
import com.studymate.backend.member.dto.MemberResponse;
import com.studymate.backend.member.service.MemberService;
import com.studymate.backend.notification.service.NotificationService;
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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
@Slf4j
@RequiredArgsConstructor
public class MatchingService {
    private final NotificationService notificationService;
    private final MemberService memberService;
    private final QuestionRepository questionRepository;
    private final PushNotificationService pushNotificationService;
    private final MemberRepository memberRepository;
    private final ReviewRepository reviewRepository;
    private final ReviewMapper reviewMapper;
    private final ChatService chatService;

    private final RestTemplate restTemplate;
    private final MemberMapper memberMapper;
    private final static String FIRST = "이건 질문자가 질문한 내용인데 이 내용을 기반으로 가장 적절한 멘토를 출력해. 멘토들의 정보 : ";
    private final static String FINAL = "이제 여기서 가장 질문과 맞는 멘토를 선택 한 후에 반환해줘. 반환값의 경우 id값만 출력해 만약 id값이 2개 이상이라면 공백으로 구분해(예시로 가장 일치하는 멘토들의 id가 1,2,5 라면 반환값은 1 2 5)";
    @Value("${openai.model}")
    private String model;
    @Value("${openai.api.url}")
    private String apiURL;






    @Value("${coolsms.apikey}")
    private String apiKey;
    @Value("${coolsms.apisecret}")
    private String apiSecret;
    @Value("${coolsms.fromnumber}")
    private String fromNumber;
    private static final String pattern = "\\d+(\\s\\d+)*";

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

    @Transactional
    public String matchingForSms(Long questionId, Long mentorId) {
        Member member = memberService.getMember();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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




        MatchingSseResponse matchingSseResponse = MatchingSseResponse.builder()
                .nickname(member.getNickname()) // 사용자 닉네임
                .question_id(question.getId()) // 게시물 ID
                .likedTime(LocalDateTime.now(ZoneId.of("Asia/Seoul")).format(formatter))
                .build();

        notificationService.customNotify(mentor, matchingSseResponse, "매칭 요청을 보냈습니다.", "Matching");

        // 채팅방 생성
        Long chatRoomId = chatService.createChatRoom(mentor.getNickname() + " & " + question.getMember().getNickname()).getId();
        // 멘토와 멘티 채팅방 참여
        chatService.addUserToRoom(chatRoomId, mentorId);
        chatService.addUserToRoom(chatRoomId, question.getMember().getId());

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

    /**
     * 1. 질문(상세분야 키워드 뽑아옴) -> "데이터베이스 JPA 스프링" (프론트에서 분리단위 공백으로 구분)
     * 2. 일단 질문 분야와 같은 멘토 불러옴
     * 3.1. 서버에서 받아온 상세분야 " " 기준으로 슬라이싱 처리 후 컬렉션에 저장
     * 3.2. 저장된 컬렉션 인덱스 길이만큼 반복문 실행
     * 3.3. 문자열이 일치하다고 판단(KMP)되면 저장변수에 + 1
     * 4. 모든 검색을 마친 후 가장 높은 점수를 받은 멘토 들을 내림차순으로 출력.
     */
    public List<MemberResponse> getMentorListByKeyword(Long questionId) {
        //1
        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("not found question id"));

        String specificField = question.getSpecificField();

        //2
        MemberListResponse listResponses = getMentorList(questionId);
        List<MemberResponse> mentorList = listResponses.getMemberList();

        //3.1
        String[] tokens = specificField.split("\\s+");
        List<String> field = new ArrayList<>();
        Collections.addAll(field, tokens);

        //3.2 ~ 3.3
        Map<MemberResponse, Integer> matchingCountsByPR = new HashMap<>();
        Map<MemberResponse, Integer> matchingCountsByExField = new HashMap<>();

        List<MemberResponse> result = new ArrayList<>();
        for (MemberResponse mentor : mentorList) {
            int matchingCountByPR = 0;
            int matchingCountByExField = 0;

            for (String s : field) {
                if (KMPSearch(mentor.getPublicRelations(), s)) {
                    matchingCountByPR++;
                    log.info("matchingToken: {}", s);
                    log.info("mentorsPR: {}", mentor.getPublicRelations());
                }
                if (KMPSearch(mentor.getExpertiseField(), s)) {
                    matchingCountByExField++;
                    log.info("matchingToken: {}", s);
                    log.info("mentorsExField: {}", mentor.getExpertiseField());
                }
            }
            if (matchingCountByPR > 0) {
                matchingCountsByPR.put(mentor, matchingCountByPR);
            }
            if (matchingCountByExField > 0) {
                matchingCountsByExField.put(mentor, matchingCountByPR);
            }
            if (matchingCountByPR > 0 || matchingCountByExField > 0) {
                log.info("mentor:{}", mentor.getNickname());
                log.info("mentorsPRCount:{}", matchingCountByPR);
                log.info("mentorsExCount:{}", matchingCountByExField);
                log.info("SUM:{}", matchingCountByPR + matchingCountByExField);
                result.add(mentor);
            }
        }

        //4
        result.sort((a, b) -> {
            int aValue = matchingCountsByPR.getOrDefault(a, 0) + matchingCountsByExField.getOrDefault(a, 0);
            int bValue = matchingCountsByPR.getOrDefault(b, 0) + matchingCountsByExField.getOrDefault(b, 0);
            return Integer.compare(bValue, aValue);
        });

        if (result.isEmpty()) {
            mentorList.sort(Comparator.comparingInt(MemberResponse::getHeart).reversed());
            result.addAll(mentorList.subList(0, Math.min(5, mentorList.size())));
        }

        return result;
    }

    private static boolean KMPSearch(String text, String pattern) {
        int[] lps = computeLPSArray(pattern);
        int i = 0;  // text의 인덱스
        int j = 0;  // pattern의 인덱스

        while (i < text.length()) {
            if (pattern.charAt(j) == text.charAt(i)) {
                i++;
                j++;
            }
            if (j == pattern.length()) {
                return true; // 패턴 발견
            } else if (i < text.length() && pattern.charAt(j) != text.charAt(i)) {
                if (j != 0)
                    j = lps[j - 1];
                else
                    i = i + 1;
            }
        }
        return false; // 패턴 미발견
    }

    private static int[] computeLPSArray(String pattern) {
        int[] lps = new int[pattern.length()];
        int len = 0;
        int i = 1;

        while (i < pattern.length()) {
            if (pattern.charAt(i) == pattern.charAt(len)) {
                len++;
                lps[i] = len;
                i++;
            } else {
                if (len != 0) {
                    len = lps[len - 1];
                } else {
                    lps[i] = len;
                    i++;
                }
            }
        }
        return lps;
    }

    public List<MemberResponse> getMentorListByAi(Long questionId) {
        List<MemberResponse> memberResponses = new ArrayList<>();
        // 2
        MemberListResponse mentorList = getMentorList(questionId);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("not found question"));

        String questionContent = question.getContent();

        String mentorInfo = convertToJsonString(mentorList);

        // 3
        String message = gptConvert(questionContent, mentorInfo);
        boolean isMatch = patternMatch(message);

        log.info("content:{}",message);
        log.info("isMatch:{}",isMatch);

        // 형식 검증 로직
        if (!isMatch) {
            while (!isMatch) {
                message = gptConvert(questionContent, mentorInfo);
                isMatch = patternMatch(message);
                log.info("Change message :{}",message);
                log.info("Change isMatch:{}",isMatch);
            }
        }

        String[] parts = message.split(" ");
        long[] numbers = new long[parts.length];

        log.info("numbers:{}", numbers);

        for (int i = 0; i < parts.length; i++) {
            numbers[i] = Long.parseLong(parts[i]); // 각 부분을 정수로 변환하여 저장
        }

        for (long number : numbers) {
            Member member = memberRepository.findById(number)
                    .orElseThrow(() -> new RuntimeException("not found member"));

            MemberResponse memberResponse = memberMapper.toResponse(member);
            memberResponses.add(memberResponse);
        }

        return memberResponses;
    }


    public static String convertToJsonString(MemberListResponse mentorList) {
        ObjectMapper objectMapper = new ObjectMapper();

        List<JsonMentorResponse> jsonMentorResponses = new ArrayList<>();

        for (MemberResponse memberResponse : mentorList.getMemberList()) {
            JsonMentorResponse jsonResponse = JsonMentorResponse.builder()
                    .id(memberResponse.getId())
                    .expertiseField(memberResponse.getExpertiseField())
                    .publicRelations(memberResponse.getPublicRelations())
                    .build();

            jsonMentorResponses.add(jsonResponse);
        }
        try {
            return objectMapper.writeValueAsString(jsonMentorResponses);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
            return null;
        }
    }

    public String gptConvert(String questionContent, String mentorInfo) {
        GPTRequest gptRequest = new GPTRequest(model, questionContent+FIRST+mentorInfo+FINAL,
                1, 256, 1, 2, 2);
        GPTResponse response = restTemplate.postForObject(apiURL, gptRequest, GPTResponse.class);

        return response.getChoices().get(0).getMessage().getContent();
    }

    public boolean patternMatch(String message) {
        Pattern compile = Pattern.compile(pattern);
        Matcher matcher = compile.matcher(message);
        boolean isMatch = matcher.matches();
        return isMatch;
    }
}

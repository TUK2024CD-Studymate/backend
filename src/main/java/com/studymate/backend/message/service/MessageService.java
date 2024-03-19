package com.studymate.backend.message.service;

import com.studymate.backend.message.dto.MessageRequest;
import com.studymate.backend.message.dto.VerifyRequest;
import com.studymate.backend.message.repository.SmsCertification;
import lombok.RequiredArgsConstructor;
import net.nurigo.java_sdk.api.Message;
import net.nurigo.java_sdk.exceptions.CoolsmsException;
import org.json.simple.JSONObject;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Random;

@Service
@RequiredArgsConstructor
public class MessageService {

    private final SmsCertification smsCertification;

    @Value("${coolsms.apikey}")
    private String apiKey;

    @Value("${coolsms.apisecret}")
    private String apiSecret;

    @Value("${coolsms.fromnumber}")
    private String fromNumber;

    private String createRandomNumber() {
        Random rand = new Random();
        String randomNum = "";
        for (int i = 0; i < 4; i++) {
            String random = Integer.toString(rand.nextInt(10));
            randomNum += random;
        }

        return randomNum;
    }

    private HashMap<String, String> makeParams(String to, String randomNum) {
        HashMap<String, String> params = new HashMap<>();
        params.put("from", fromNumber);
        params.put("type", "SMS");
        params.put("app_version", "test app 1.2");
        params.put("to", to);
        params.put("text", "[StudyMate] 인증번호 "+randomNum);
        return params;
    }

    // 인증번호 전송하기
    public String sendSMS(VerifyRequest request) {
        Message coolsms = new Message(apiKey, apiSecret);

        // 랜덤한 인증 번호 생성
        String randomNum = createRandomNumber();
        System.out.println(randomNum);

        // 발신 정보 설정
        HashMap<String, String> params = makeParams(request.getTel(), randomNum);

        try {
            JSONObject obj = (JSONObject) coolsms.send(params);
            System.out.println(obj.toString());
        } catch (CoolsmsException e) {
            System.out.println(e.getMessage());
            System.out.println(e.getCode());
        }

        smsCertification.createSmsCertification(request.getTel(), randomNum);

        return "문자 전송이 완료되었습니다.";
    }

    // 인증 번호 검증
    public String verifySms(MessageRequest requestDto) {
        if (isVerify(requestDto)) {
            throw new IllegalArgumentException("인증번호가 일치하지 않습니다.");
        }
        smsCertification.deleteSmsCertification(requestDto.getPhoneNumber());

        return "인증 완료되었습니다.";
    }

    private boolean isVerify(MessageRequest reqeust) {
        return !(smsCertification.hasKey(reqeust.getPhoneNumber()) &&
                smsCertification.getSmsCertification(reqeust.getPhoneNumber())
                        .equals(reqeust.getRandomNumber()));
    }
}

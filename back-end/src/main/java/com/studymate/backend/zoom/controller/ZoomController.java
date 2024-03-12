package com.studymate.backend.zoom.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.studymate.backend.zoom.ZoomTokenRepository;
import com.studymate.backend.zoom.dto.ZoomMeetingObjectDTO;
import com.studymate.backend.zoom.dto.ZoomMeetingSettingsDTO;
import com.studymate.backend.zoom.service.ZoomService;
import com.studymate.backend.zoom.util.DecEncUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import okhttp3.*;
import org.springframework.http.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequiredArgsConstructor
public class ZoomController {
    private final ZoomService zoomService;
    private final ZoomTokenRepository zoomTokenRepository;

    @RequestMapping(value = "/api/meeting/zoomApi", method = {RequestMethod.GET
            , RequestMethod.POST})
    public String googleAsync(HttpServletRequest req,
                                         @RequestParam(required = false) String code) throws
            IOException, NoSuchAlgorithmException {

        String zoomUrl = "https://zoom.us/oauth/token";
        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        FormBody formBody = new FormBody.Builder()
                .add("code", code)
                .add("redirect_uri", "http://studymate-tuk.kro.kr:8080/api/meeting/zoomApi")
                .add("grant_type", "authorization_code")
                .add("code_verifier", DecEncUtil.encode(code))
                .build();
        Request zoomRequest = new Request.Builder()
                .url(zoomUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization", "Basic " + "Wmd0ODlLaVpScmk4U2tCcXdzMFNSZzo1WGNHNXY2bFF5MkJUNmFXMFN2MmdZMWRseWIyYU5Udg==")
                .post(formBody)
                .build();

        Response zoomResponse = client.newCall(zoomRequest).execute();
        String zoomText = zoomResponse.body().string();

        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        Map<String, String> list = mapper.readValue(zoomText, new TypeReference<>() {
        });
        String accessToken = list.get("access_token");
        String refreshToken = list.get("refresh_token");
        zoomService.saveToken(accessToken, refreshToken);
        return "zoomLogin";
    }

    @GetMapping("/api/meeting/create")
    public ResponseEntity<?> createMeeting() throws IOException {

        isExpired();

        ZoomMeetingObjectDTO zoomMeetingObjectDTO = new ZoomMeetingObjectDTO();

        String apiUrl = "https://api.zoom.us/v2/users/" + "me" + "/meetings";

        ZoomMeetingSettingsDTO settingsDTO = new ZoomMeetingSettingsDTO();
        settingsDTO.setJoin_before_host(false);
        settingsDTO.setParticipant_video(true);
        settingsDTO.setHost_video(false);
        settingsDTO.setAuto_recording("cloud");
        settingsDTO.setMute_upon_entry(true);
        zoomMeetingObjectDTO.setType(1);
        settingsDTO.setMeeting_authentication(true);

        zoomMeetingObjectDTO.setSettings(settingsDTO);


        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", "Bearer " + zoomService.findAccessToken());
        headers.add("content-type", "application/json");
        HttpEntity<ZoomMeetingObjectDTO> httpEntity = new HttpEntity<ZoomMeetingObjectDTO>(zoomMeetingObjectDTO, headers);
        ResponseEntity<ZoomMeetingObjectDTO> zEntity = restTemplate.exchange(apiUrl, HttpMethod.POST, httpEntity, ZoomMeetingObjectDTO.class);
        if (zEntity.getStatusCodeValue() == 201) {
            Map<String, Object> meetCredentials = new HashMap<>();
            meetCredentials.put("start_url", zEntity.getBody().getStart_url());
            meetCredentials.put("join_url", zEntity.getBody().getJoin_url());
            return ResponseEntity.ok().body(meetCredentials);
        }

        Map<String, Object> meetCredentials = new HashMap<>();
        meetCredentials.put("start_url", zoomMeetingObjectDTO.getStart_url());
        meetCredentials.put("join_url", zoomMeetingObjectDTO.getJoin_url());
        return ResponseEntity.ok().body(meetCredentials);
    }

    public ResponseEntity<?> refreshToken() throws IOException {
        String zoomUrl = "https://zoom.us/oauth/token";

        OkHttpClient client = new OkHttpClient();
        ObjectMapper mapper = new ObjectMapper();

        FormBody formBody = new FormBody.Builder()
                .add("grant_type", "refresh_token")
                .add("refresh_token", zoomService.findRefreshToken())
                .build();
        Request zoomRequest = new Request.Builder()
                .url(zoomUrl)
                .addHeader("Content-Type", "application/x-www-form-urlencoded")
                .addHeader("Authorization","Basic " + "Wmd0ODlLaVpScmk4U2tCcXdzMFNSZzo1WGNHNXY2bFF5MkJUNmFXMFN2MmdZMWRseWIyYU5Udg==")
                .post(formBody)
                .build();

        Response zoomResponse = client.newCall(zoomRequest).execute();
        String zoomText = zoomResponse.body().string();

        mapper.configure(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY, true);

        Map<String, String> list = mapper.readValue(zoomText, new TypeReference<>() {});
        String accessToken = list.get("access_token");
        String refreshToken = list.get("refresh_token");
        return ResponseEntity.ok().body(list);
    }
    public void isExpired() throws IOException {
        // 현재 시간 가져오기
        LocalDateTime now = LocalDateTime.now();

        // updatedAt과 현재 시간의 차이 계산 (단위: 분)
        long minutesSinceUpdate = ChronoUnit.MINUTES.between(zoomTokenRepository.findById(0L).get().getUpdatedAt(), now);

        // 만료 시간을 60분으로 설정
        long expirationTimeInMinutes = 60;

        // 현재 시간과 updatedAt의 차이가 expirationTimeInMinutes 이상인 경우 만료되었다고 판단
        // updatedAt 시간이 현재 시간으로부터 60분 이상 경과했다면 true를 반환하며, 그렇지 않으면 false를 반환합니다.
        boolean checkExpired = minutesSinceUpdate >= expirationTimeInMinutes;
        // 토큰 재발급
        if(checkExpired) {
            refreshToken();
        }
    }
}

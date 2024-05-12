package com.studymate.backend.zoom.service;

import com.studymate.backend.zoom.ZoomTokenRepository;
import com.studymate.backend.zoom.domain.ZoomMeeting;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ZoomService {

    private final ZoomTokenRepository zoomTokenRepository;

    @Transactional
    public void saveToken(String accessToken, String refreshToken) {


        ZoomMeeting zoomMeeting = ZoomMeeting.builder()
                .id(0L)
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .build();

        zoomTokenRepository.save(zoomMeeting);
    }

    @Transactional
    public String findAccessToken() {

        String accessToken = zoomTokenRepository.findById(0L).get().getAccessToken();

        return accessToken;
    }

    @Transactional
    public String findRefreshToken() {
        String refreshToken = zoomTokenRepository.findById(0L).get().getRefreshToken();

        return refreshToken;
    }
}

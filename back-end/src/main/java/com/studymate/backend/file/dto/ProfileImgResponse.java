package com.studymate.backend.file.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ProfileImgResponse {
    private String url;
    @Builder
    public ProfileImgResponse(String url) {
        this.url = url;
    }
}

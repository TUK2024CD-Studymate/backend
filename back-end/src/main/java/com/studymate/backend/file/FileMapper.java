package com.studymate.backend.file;

import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.member.domain.Member;
import org.springframework.stereotype.Component;

@Component
public class FileMapper {

    public ProfileImg toEntity(String url, Member member) {
        return ProfileImg.builder()
                .member(member)
                .url(url)
                .build();
    }
}

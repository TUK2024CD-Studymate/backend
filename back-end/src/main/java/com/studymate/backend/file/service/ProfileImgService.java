package com.studymate.backend.file.service;

import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.file.dto.ProfileImgResponse;
import com.studymate.backend.file.dto.ProfileImgUpload;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ProfileImgService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileImgService.class);

    private final MemberRepository memberRepository;
    private final ProfileImgRepository profileImgRepository;

    @Value("${file.profileImagePath}")
    private String uploadFolder;

    public void upload(ProfileImgUpload upload, String email) {
        Member member = memberRepository.findOneWithAuthoritiesByEmail(email).orElseThrow(() ->
                new UsernameNotFoundException("조회 실패"));
        MultipartFile file = upload.getFile();

        UUID uuid = UUID.randomUUID();
        String imageFileName = uuid + "_" + file.getOriginalFilename();

        File destinationFile = new File(uploadFolder + imageFileName);

        try {
            file.transferTo(destinationFile);

            ProfileImg image = profileImgRepository.findByMember(member);

            if (image != null) {
                image.updateUrl("/profileImages/" + imageFileName);
            } else {
                image = ProfileImg.builder()
                        .member(member)
                        .url("/profileImages/" + imageFileName)
                        .build();

            }
            profileImgRepository.save(image);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public ProfileImgResponse findImage(String email) {
        Member member = memberRepository.findOneWithAuthoritiesByEmail(email).orElseThrow(() ->
                new RuntimeException("조회 실패"));
        ProfileImg image = profileImgRepository.findByMember(member);

        String defaultImageUrl = "/profileImages/anonymous.png";

        if (image == null) {
            return ProfileImgResponse.builder()
                    .url(defaultImageUrl)
                    .build();
        } else {
            return ProfileImgResponse.builder()
                    .url(image.getUrl())
                    .build();
        }
    }
}

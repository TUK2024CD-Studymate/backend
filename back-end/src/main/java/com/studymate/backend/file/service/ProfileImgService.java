package com.studymate.backend.file.service;

import com.studymate.backend.config.security.SecurityUtil;
import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.member.MemberRepository;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.exception.NotFoundMemberException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProfileImgService {
    private final MemberRepository memberRepository;
    private final ProfileImgRepository profileImgRepository;

    @Transactional
    public String upload(MultipartFile upload) throws IOException {

        Member member = SecurityUtil.getCurrentUsername()
                .flatMap(memberRepository::findOneWithAuthoritiesByEmail)
                .orElseThrow(() -> new NotFoundMemberException("Member not found"));

        ProfileImg image = profileImgRepository.findByMember(member);

        image.upload(upload.getOriginalFilename(), upload.getContentType(), upload.getBytes());

        if (image != null) {
            log.info("imageDataName: {}", image.getName());
            return "업로드 성공 : " + upload.getOriginalFilename();
        }

        return null;
    }
}


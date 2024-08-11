package com.studymate.backend.file.service;

import com.amazonaws.SdkClientException;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.studymate.backend.file.FileMapper;
import com.studymate.backend.file.ProfileImgRepository;
import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.file.dto.S3Url;
import com.studymate.backend.member.domain.Member;
import com.studymate.backend.member.service.MemberService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
@Transactional
public class S3UploadService {
    private final MemberService memberService;
    private final ProfileImgRepository profileImgRepository;
    private final FileMapper fileMapper;
    private final AmazonS3 amazonS3;
    @Value("${cloud.aws.s3.bucket}")
    private String bucket;

    public S3Url saveFile(MultipartFile multipartFile) throws IOException {
        Member member = memberService.getMember();

        profileImgRepository.findByMember(member).ifPresent(profileImg -> {
            deleteProfile(profileImg);
        });
        profileImgRepository.flush();

        return uploadS3(multipartFile, member);
    }

    public S3Url uploadS3(MultipartFile multipartFile, Member member) throws IOException {
        String originalFilename = multipartFile.getOriginalFilename();

        // 한글 처리
        String encodeFileName = URLEncoder.encode(originalFilename, StandardCharsets.UTF_8);

        String randomFilename = generateRandomFilename(encodeFileName);
        log.info("filename: {}", randomFilename);

        ObjectMetadata metadata = new ObjectMetadata();
        metadata.setContentLength(multipartFile.getSize());
        metadata.setContentType(multipartFile.getContentType());

        amazonS3.putObject(bucket, randomFilename, multipartFile.getInputStream(), metadata);

        String s3UrlString = amazonS3.getUrl(bucket, randomFilename).toString();
        log.info("s3Url: {}", s3UrlString);

        S3Url s3Url = S3Url.builder().s3Url(s3UrlString).build();
        profileImgRepository.save(fileMapper.toEntity(s3Url.getS3Url(), member));
        return s3Url;
    }

    private String generateRandomFilename(String fileName) {
        return UUID.randomUUID() + fileName;
    }

    public void deleteProfile(ProfileImg profileImg) {
        deleteFile(profileImg.getUrl());
        profileImgRepository.delete(profileImg);
        log.info("삭제완료 로그");
    }

    public void deleteFile(String fileUrl) {
        String splitStr = ".com/";
        String fileName = fileUrl.substring(fileUrl.lastIndexOf(splitStr) + splitStr.length());
        log.info("split URL: {}", fileName);
        String decodeFileName = URLDecoder.decode(fileName, StandardCharsets.UTF_8);
        log.info("split URL: {}", decodeFileName);

        try {
            amazonS3.deleteObject(bucket, decodeFileName);
        } catch (AmazonS3Exception e) {
            log.error("File delete fail : " + e.getMessage());
            throw new RuntimeException("FAIL_DELETE");
        } catch (SdkClientException e) {
            log.error("AWS SDK client error : " + e.getMessage());
            throw new RuntimeException("FAIL_DELETE");
        }

        log.info("File delete complete: " + decodeFileName);
    }
}

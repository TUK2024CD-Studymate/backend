package com.studymate.backend.file.controller;

import com.studymate.backend.file.dto.S3Url;
import com.studymate.backend.file.service.S3UploadService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/image")
@Tag(name = "회원", description = "회원 API")
public class ProfileImgController {
    private final S3UploadService s3UploadService;

    @PutMapping("/upload")
    @Operation(summary = "회원이 프로필 사진을 업로드한다.", description = "회원이 프로필 사진업로드 한다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<S3Url> upload(@RequestPart(value = "image", required = false) MultipartFile profileImgUpload) throws IOException {
        return ResponseEntity.status(HttpStatus.OK).body(s3UploadService.saveFile(profileImgUpload));
    }

//    @DeleteMapping("/delete")
//    @Operation(summary = "회원이 프로필 사진을 삭제한다.", description = "회원이 프로필 사진을 삭제한다.")
//    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
//    public ResponseEntity<String> delete() throws IOException {
//        return ResponseEntity.status(HttpStatus.OK).body(s3UploadService.deleteProfile());
//    }
}

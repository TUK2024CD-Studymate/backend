package com.studymate.backend.file.controller;

import com.studymate.backend.file.service.ProfileImgService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequiredArgsConstructor
@RequestMapping("/image")
@Tag(name = "Member", description = "Member API")
public class ProfileImgController {

    private final ProfileImgService profileImgService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('USER')")
    @Operation(summary = "member profile image update", description = "회원이 프로필 사진을 바꾼다.")
    @ApiResponses(value = @ApiResponse(responseCode = "200", description = "성공"))
    public ResponseEntity<?> upload(@RequestParam("profileUrl") MultipartFile profileImgUpload) throws IOException {
        String image = profileImgService.upload(profileImgUpload);
        return ResponseEntity.status(HttpStatus.OK)
                .body(image);
    }
}

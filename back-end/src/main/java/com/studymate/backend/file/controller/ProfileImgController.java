package com.studymate.backend.file.controller;

import com.studymate.backend.file.service.ProfileImgService;
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
public class ProfileImgController {

    private final ProfileImgService profileImgService;

    @PostMapping("/upload")
    @PreAuthorize("hasAnyRole('USER')")
    public ResponseEntity<?> upload(@RequestParam("profileUrl") MultipartFile profileImgUpload) throws IOException {
        String image = profileImgService.upload(profileImgUpload);
        return ResponseEntity.status(HttpStatus.OK)
                .body(image);
    }
}

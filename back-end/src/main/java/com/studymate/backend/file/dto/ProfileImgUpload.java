package com.studymate.backend.file.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class ProfileImgUpload {
    private MultipartFile file;
}

package com.studymate.backend.file.dto;

import lombok.*;

@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class S3Url {
    private String s3Url;
}

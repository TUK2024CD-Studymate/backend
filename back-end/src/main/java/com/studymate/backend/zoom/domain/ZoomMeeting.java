package com.studymate.backend.zoom.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.member.domain.Member;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ZoomMeeting extends BaseTimeEntity {

    @Id
    private Long id;

    @Column(length = 1000)
    private String accessToken;

    @Column(length = 1000)
    private String refreshToken;
}

package com.studymate.backend.studycalender.domain;

import com.studymate.backend.global.BaseTimeEntity;
import com.studymate.backend.studycalender.dto.SubjectUpdateRequest;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class CalenderSubject extends BaseTimeEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "subject_id")
    private Long id;

    private String subjectName;

    public void update(SubjectUpdateRequest request) {
        this.subjectName = request.getSubjectName();
    }
}

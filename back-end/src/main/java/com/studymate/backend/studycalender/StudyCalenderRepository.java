package com.studymate.backend.studycalender;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.studycalender.domain.StudyCalender;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StudyCalenderRepository extends JpaRepository<StudyCalender, Long> {
    StudyCalender findByMemberAndId(Member member, Long id);
}

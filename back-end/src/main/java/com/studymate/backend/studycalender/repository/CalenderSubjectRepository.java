package com.studymate.backend.studycalender.repository;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.studycalender.domain.CalenderSubject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CalenderSubjectRepository extends JpaRepository<CalenderSubject, Long> {
    List<CalenderSubject> findAllByMember(Member member);
}

package com.studymate.backend.question;

import com.studymate.backend.member.domain.Member;
import com.studymate.backend.question.domain.Question;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    List<Question> findAllByMember(Member member);
}

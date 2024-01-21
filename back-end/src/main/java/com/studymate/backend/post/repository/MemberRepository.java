package com.studymate.backend.post.repository;
import com.studymate.backend.post.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface MemberRepository extends JpaRepository<Member, Integer>{
    Member findByName(String name);
}

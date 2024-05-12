package com.studymate.backend.file;

import com.studymate.backend.file.domain.ProfileImg;
import com.studymate.backend.member.domain.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProfileImgRepository extends JpaRepository<ProfileImg, Long> {
//    ProfileImg findByMember(Member member);
}

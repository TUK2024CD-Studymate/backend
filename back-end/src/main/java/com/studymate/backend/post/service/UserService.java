package com.studymate.backend.post.service;

import com.studymate.backend.post.domain.entity.Member;
import com.studymate.backend.post.dto.RegisterDto;
import com.studymate.backend.post.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final MemberRepository memberRepository;

    public Member register(RegisterDto registerDto) {
        Member user = new Member();
        user.setName(registerDto.getName());
        user.setPassword(registerDto.getPassword());
        user.setName(registerDto.getName());
        return memberRepository.save(user);
    }

    public List<Member> findAll() {
        return memberRepository.findAll();
    }

    public Member findUser(int id) {
        return memberRepository.findById(id).orElseThrow(()-> {
            return new IllegalArgumentException("User ID를 찾을 수 없습니다.");
        });
    }
}

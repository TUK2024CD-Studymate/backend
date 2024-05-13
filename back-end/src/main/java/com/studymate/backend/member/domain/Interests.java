package com.studymate.backend.member.domain;


public enum Interests {

    WEBAPP("웹/앱"),
    SERVER("서버/네트워크"),
    AI("AI/IOT"),
    DATA("데이터개발"),
    SECURITY("보안");

    private final String korean;

    Interests(String korean) {
        this.korean = korean;
    }

    public String getKorean() {
        return korean;
    }

}

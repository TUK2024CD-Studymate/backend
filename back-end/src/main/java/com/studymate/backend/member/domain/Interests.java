package com.studymate.backend.member.domain;


public enum Interests {

    PROGRAMMING("코딩"),
    MATH("수학"),
    ENGLISH("영어"),
    SCIENCE("과학"),
    KOREAN("국어");

    private final String korean;

    Interests(String korean) {
        this.korean = korean;
    }
}

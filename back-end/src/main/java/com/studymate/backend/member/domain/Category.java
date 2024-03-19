package com.studymate.backend.member.domain;

public enum Category {
    QUESTION("질문"),
    STUDY("스터디"),
    FREE("자유");

    private final String value;

    Category(String value) {
        this.value = value;
    }
}

package com.studymate.backend.member.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMember is a Querydsl query type for Member
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMember extends EntityPathBase<Member> {

    private static final long serialVersionUID = 370642071L;

    public static final QMember member = new QMember("member1");

    public final com.studymate.backend.global.QBaseTimeEntity _super = new com.studymate.backend.global.QBaseTimeEntity(this);

    public final BooleanPath activated = createBoolean("activated");

    public final SetPath<Authority, QAuthority> authorities = this.<Authority, QAuthority>createSet("authorities", Authority.class, QAuthority.class, PathInits.DIRECT2);

    public final StringPath blogUrl = createString("blogUrl");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final StringPath email = createString("email");

    public final StringPath fcmToken = createString("fcmToken");

    public final NumberPath<Integer> heart = createNumber("heart", Integer.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final EnumPath<Interests> interests = createEnum("interests", Interests.class);

    public final BooleanPath isDeleted = createBoolean("isDeleted");

    public final StringPath job = createString("job");

    public final NumberPath<Integer> matchingCount = createNumber("matchingCount", Integer.class);

    public final StringPath name = createString("name");

    public final StringPath nickname = createString("nickname");

    public final EnumPath<Part> part = createEnum("part", Part.class);

    public final StringPath password = createString("password");

    public final StringPath publicRelations = createString("publicRelations");

    public final NumberPath<Integer> reviewCount = createNumber("reviewCount", Integer.class);

    public final NumberPath<Integer> solved = createNumber("solved", Integer.class);

    public final NumberPath<Integer> star = createNumber("star", Integer.class);

    public final NumberPath<Double> starAverage = createNumber("starAverage", Double.class);

    public final ListPath<com.studymate.backend.studycalender.domain.StudyCalender, com.studymate.backend.studycalender.domain.QStudyCalender> studyCalenders = this.<com.studymate.backend.studycalender.domain.StudyCalender, com.studymate.backend.studycalender.domain.QStudyCalender>createList("studyCalenders", com.studymate.backend.studycalender.domain.StudyCalender.class, com.studymate.backend.studycalender.domain.QStudyCalender.class, PathInits.DIRECT2);

    public final StringPath tel = createString("tel");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMember(String variable) {
        super(Member.class, forVariable(variable));
    }

    public QMember(Path<? extends Member> path) {
        super(path.getType(), path.getMetadata());
    }

    public QMember(PathMetadata metadata) {
        super(Member.class, metadata);
    }

}


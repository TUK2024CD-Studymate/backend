package com.studymate.backend.studycalender.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QStudyCalender is a Querydsl query type for StudyCalender
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QStudyCalender extends EntityPathBase<StudyCalender> {

    private static final long serialVersionUID = -1424577813L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QStudyCalender studyCalender = new QStudyCalender("studyCalender");

    public final com.studymate.backend.global.QBaseTimeEntity _super = new com.studymate.backend.global.QBaseTimeEntity(this);

    public final StringPath content = createString("content");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> endTime = createDateTime("endTime", java.time.LocalDateTime.class);

    public final TimePath<java.time.LocalTime> entireTime = createTime("entireTime", java.time.LocalTime.class);

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.studymate.backend.member.domain.QMember member;

    public final DateTimePath<java.time.LocalDateTime> startTime = createDateTime("startTime", java.time.LocalDateTime.class);

    public final EnumPath<com.studymate.backend.member.domain.Interests> studyClass = createEnum("studyClass", com.studymate.backend.member.domain.Interests.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QStudyCalender(String variable) {
        this(StudyCalender.class, forVariable(variable), INITS);
    }

    public QStudyCalender(Path<? extends StudyCalender> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QStudyCalender(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QStudyCalender(PathMetadata metadata, PathInits inits) {
        this(StudyCalender.class, metadata, inits);
    }

    public QStudyCalender(Class<? extends StudyCalender> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.studymate.backend.member.domain.QMember(forProperty("member")) : null;
    }

}


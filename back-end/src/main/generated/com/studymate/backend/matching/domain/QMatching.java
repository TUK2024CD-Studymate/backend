package com.studymate.backend.matching.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;
import com.querydsl.core.types.dsl.PathInits;


/**
 * QMatching is a Querydsl query type for Matching
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QMatching extends EntityPathBase<Matching> {

    private static final long serialVersionUID = 811939933L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMatching matching = new QMatching("matching");

    public final com.studymate.backend.global.QBaseTimeEntity _super = new com.studymate.backend.global.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final com.studymate.backend.member.domain.QMember member;

    public final com.studymate.backend.question.domain.QQuestion question;

    public final NumberPath<Long> receiverId = createNumber("receiverId", Long.class);

    public final NumberPath<Long> senderId = createNumber("senderId", Long.class);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QMatching(String variable) {
        this(Matching.class, forVariable(variable), INITS);
    }

    public QMatching(Path<? extends Matching> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMatching(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMatching(PathMetadata metadata, PathInits inits) {
        this(Matching.class, metadata, inits);
    }

    public QMatching(Class<? extends Matching> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.member = inits.isInitialized("member") ? new com.studymate.backend.member.domain.QMember(forProperty("member")) : null;
        this.question = inits.isInitialized("question") ? new com.studymate.backend.question.domain.QQuestion(forProperty("question"), inits.get("question")) : null;
    }

}


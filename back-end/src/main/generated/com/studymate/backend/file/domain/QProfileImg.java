package com.studymate.backend.file.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QProfileImg is a Querydsl query type for ProfileImg
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QProfileImg extends EntityPathBase<ProfileImg> {

    private static final long serialVersionUID = 2044371193L;

    public static final QProfileImg profileImg = new QProfileImg("profileImg");

    public final com.studymate.backend.global.QBaseTimeEntity _super = new com.studymate.backend.global.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final ArrayPath<byte[], Byte> imageData = createArray("imageData", byte[].class);

    public final StringPath name = createString("name");

    public final StringPath type = createString("type");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QProfileImg(String variable) {
        super(ProfileImg.class, forVariable(variable));
    }

    public QProfileImg(Path<? extends ProfileImg> path) {
        super(path.getType(), path.getMetadata());
    }

    public QProfileImg(PathMetadata metadata) {
        super(ProfileImg.class, metadata);
    }

}


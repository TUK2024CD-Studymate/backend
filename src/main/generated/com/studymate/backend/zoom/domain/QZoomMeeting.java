package com.studymate.backend.zoom.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QZoomMeeting is a Querydsl query type for ZoomMeeting
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QZoomMeeting extends EntityPathBase<ZoomMeeting> {

    private static final long serialVersionUID = 226714482L;

    public static final QZoomMeeting zoomMeeting = new QZoomMeeting("zoomMeeting");

    public final com.studymate.backend.global.QBaseTimeEntity _super = new com.studymate.backend.global.QBaseTimeEntity(this);

    public final StringPath accessToken = createString("accessToken");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdAt = _super.createdAt;

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath refreshToken = createString("refreshToken");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> updatedAt = _super.updatedAt;

    public QZoomMeeting(String variable) {
        super(ZoomMeeting.class, forVariable(variable));
    }

    public QZoomMeeting(Path<? extends ZoomMeeting> path) {
        super(path.getType(), path.getMetadata());
    }

    public QZoomMeeting(PathMetadata metadata) {
        super(ZoomMeeting.class, metadata);
    }

}


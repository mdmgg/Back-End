package com.example.mdmggreal.member.entity;

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

    private static final long serialVersionUID = 1384281820L;

    private static final PathInits INITS = PathInits.DIRECT2;

    public static final QMember member = new QMember("member1");

    public final com.example.mdmggreal.global.entity.QBaseEntity _super = new com.example.mdmggreal.global.entity.QBaseEntity(this);

    public final com.example.mdmggreal.member.type.QAgree agree;

    //inherited
    public final DateTimePath<java.time.LocalDateTime> createdDateTime = _super.createdDateTime;

    public final StringPath email = createString("email");

    public final NumberPath<Long> id = createNumber("id", Long.class);

    public final StringPath mobile = createString("mobile");

    //inherited
    public final DateTimePath<java.time.LocalDateTime> modifyDateTime = _super.modifyDateTime;

    public final StringPath nickname = createString("nickname");

    public final EnumPath<com.example.mdmggreal.member.type.OAuthProvider> oAuthProvider = createEnum("oAuthProvider", com.example.mdmggreal.member.type.OAuthProvider.class);

    public final NumberPath<Integer> point = createNumber("point", Integer.class);

    public final StringPath profileImage = createString("profileImage");

    public final EnumPath<com.example.mdmggreal.member.type.Role> role = createEnum("role", com.example.mdmggreal.member.type.Role.class);

    public final EnumPath<com.example.mdmggreal.ingameinfo.type.Tier> tier = createEnum("tier", com.example.mdmggreal.ingameinfo.type.Tier.class);

    public QMember(String variable) {
        this(Member.class, forVariable(variable), INITS);
    }

    public QMember(Path<? extends Member> path) {
        this(path.getType(), path.getMetadata(), PathInits.getFor(path.getMetadata(), INITS));
    }

    public QMember(PathMetadata metadata) {
        this(metadata, PathInits.getFor(metadata, INITS));
    }

    public QMember(PathMetadata metadata, PathInits inits) {
        this(Member.class, metadata, inits);
    }

    public QMember(Class<? extends Member> type, PathMetadata metadata, PathInits inits) {
        super(type, metadata, inits);
        this.agree = inits.isInitialized("agree") ? new com.example.mdmggreal.member.type.QAgree(forProperty("agree")) : null;
    }

}


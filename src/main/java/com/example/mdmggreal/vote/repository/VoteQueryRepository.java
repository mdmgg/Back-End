package com.example.mdmggreal.vote.repository;


import com.example.mdmggreal.ingameinfo.entity.InGameInfo;
import com.example.mdmggreal.ingameinfo.entity.QInGameInfo;
import com.example.mdmggreal.post.entity.Post;
import com.example.mdmggreal.post.entity.QPost;
import com.example.mdmggreal.vote.entity.QVote;
import com.example.mdmggreal.vote.entity.Vote;
import com.querydsl.core.types.dsl.NumberExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.mdmggreal.ingameinfo.entity.QInGameInfo.inGameInfo;
import static com.example.mdmggreal.member.entity.QMember.member;
import static com.example.mdmggreal.post.entity.QPost.post;
import static com.example.mdmggreal.vote.entity.QVote.vote;
import static com.querydsl.jpa.JPAExpressions.select;
import static com.querydsl.jpa.JPAExpressions.selectFrom;

@Repository
@Slf4j
public class VoteQueryRepository extends QuerydslRepositorySupport {
    private final JPAQueryFactory jpaQueryFactory;

    public VoteQueryRepository(JPAQueryFactory jpaQueryFactory) {
        super(Vote.class);
        this.jpaQueryFactory = jpaQueryFactory;
    }

    public Optional<Vote> getVoteByPostIdAndMemberId(Long postId, Long memberId) {
        return Optional.ofNullable(
                from(vote)
                        .leftJoin(member).on(vote.memberId.eq(memberId))
                        .leftJoin(inGameInfo).on(vote.inGameInfo.id.eq(inGameInfo.id).and(inGameInfo.post.id.eq(postId)))
                        .fetchFirst() // 수정
        );
    }

    public List<Vote> getVoteListByPostId(Long postId) {
        QVote vote = QVote.vote;
        QVote voteSub = new QVote("voteSub");
        QInGameInfo inGameInfo = QInGameInfo.inGameInfo;
        QPost post = QPost.post;

        // 서브쿼리: 각 멤버별로 가장 높은 ratio 값을 찾음
        return selectFrom(vote)
                .leftJoin(vote.inGameInfo, inGameInfo)
                .leftJoin(inGameInfo.post, post)
                .where(post.id.eq(postId)
                        .and(vote.ratio.in(
                              select(voteSub.ratio.max())
                                        .from(voteSub)
                                        .where(voteSub.inGameInfo.post.id.eq(postId))
                                        .groupBy(voteSub.memberId)
                        )))
                .fetch();
    }

    public boolean existsVoteByMemberId(Long postId, Long memberId) {
        return
                from(vote)
                        .leftJoin(member)
                        .on(vote.memberId.eq(memberId))
                        .leftJoin(inGameInfo)
                        .on(vote.inGameInfo.id.eq(inGameInfo.id))
                        .leftJoin(post)
                        .on(post.id.eq(inGameInfo.post.id))
                        .where(post.id.eq(postId).and(member.id.eq(memberId)))
                        .fetchFirst() != null;
    }


    public InGameInfo getVoteHighRatio(Long id) {
        QVote v = QVote.vote;
        QInGameInfo igi = QInGameInfo.inGameInfo;
        QPost p = QPost.post;

        // 서브쿼리 정의
        NumberExpression<Double> avgRatio = v.ratio.avg();

        // 메인 쿼리 정의

                from(inGameInfo)
                .leftJoin(v.inGameInfo, igi)
                .leftJoin(igi.post, p)
                .where(p.id.eq(id))
                .groupBy(v.inGameInfo.id)
                .fetchOne();

        return topAvgRatio;
    }

}

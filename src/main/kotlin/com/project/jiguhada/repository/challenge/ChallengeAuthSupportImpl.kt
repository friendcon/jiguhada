package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.ChallengeAuth
import com.project.jiguhada.domain.challenge.QChallengeAuth.challengeAuth
import com.project.jiguhada.util.CHALLENGE_AUTH_STATUS
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import java.time.LocalDate

/**
 * 인증 실패하면 다시 인증할 수 있음
 * 인증 대기중이라면 이미
 */
class ChallengeAuthSupportImpl(
    private val queryFactory: JPAQueryFactory
): ChallengeAuthSupport {
    // 해당 날짜의 인증 내역 찾기
    override fun findWaitAuthByDateAndUserId(date: LocalDate, userId: Long): ChallengeAuth? {
        return queryFactory.selectFrom(challengeAuth)
            .where(
                challengeAuth.userEntity.id.eq(userId),
                challengeAuth.authDate.eq(date),
                challengeAuth.authStatus.eq(CHALLENGE_AUTH_STATUS.FAIL)
            )
            .fetchOne()
    }

    override fun findSuccessAuthByDateAndUserId(date: LocalDate, userId: Long): ChallengeAuth? {
        return queryFactory.selectFrom(challengeAuth)
            .where(
                challengeAuth.userEntity.id.eq(userId),
                challengeAuth.authDate.eq(date),
                challengeAuth.authStatus.eq(CHALLENGE_AUTH_STATUS.WAIT)
            )
            .fetchOne()
    }

    override fun findChallengeAuthList(challengeId: Long, pageable: Pageable): List<ChallengeAuth> {
        return queryFactory.selectFrom(challengeAuth)
            .where(
                challengeAuth.challenge.id.eq(challengeId)
            )
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()
    }
}
package com.project.jiguhada.repository.challenge


import com.project.jiguhada.controller.dto.challenge.ChallengeListItem
import com.project.jiguhada.controller.dto.challenge.QChallengeListItem
import com.project.jiguhada.domain.challenge.Challenge
import com.project.jiguhada.domain.challenge.QChallenge.challenge
import com.project.jiguhada.util.CHALLENGE_CATEGORY
import com.project.jiguhada.util.CHALLENGE_ORDER_TYPE
import com.project.jiguhada.util.CHALLENGE_SEARCH_TYPE
import com.project.jiguhada.util.CHALLENGE_STATUS
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.util.StringUtils
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

data class ChallengeSupportImplRepository(
    private val queryFactory: JPAQueryFactory,
    private val tagRepository: TagRepository
): QuerydslRepositorySupport(Challenge::class.java), ChallengeSupportRepository {
    override fun findChallengeList(
        query: String?,
        searchType: CHALLENGE_SEARCH_TYPE?,
        order: CHALLENGE_ORDER_TYPE?,
        category: CHALLENGE_CATEGORY?,
        status: CHALLENGE_STATUS?,
        page: Pageable
    ): List<ChallengeListItem> {
        return queryFactory.select(QChallengeListItem(
            challenge.id,
            challenge.title,
            challenge.challengeDetails,
            challenge.challengeImg,
            null,
            challenge.currrentParticipantsCount,
            challenge.participantsCount,
            challenge.challengeStartDate,
            challenge.challengePeroid,
            challenge.challengeEndDate,
            challenge.challengeStatus,
            challenge.achievementRate
        ))
            .from(challenge)
            .where(
                isquerySearchExist(query, searchType),
                checkChallengeStatus(status),
                checkChallengeCategory(category)
            )
            .orderBy(
                getOrderSpecifier(order)
            )
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()
    }

    private fun isquerySearchExist(query: String?, searchType: CHALLENGE_SEARCH_TYPE?): BooleanExpression? {
        if(StringUtils.isNullOrEmpty(query)) {
            return null
        }

        return when(searchType) {
            CHALLENGE_SEARCH_TYPE.TITLE -> challenge.title.containsIgnoreCase(query)
            CHALLENGE_SEARCH_TYPE.CONTENT -> challenge.challengeDetails.containsIgnoreCase(query)
            else -> null
        }
    }

    private fun checkChallengeCategory(category: CHALLENGE_CATEGORY?): BooleanExpression? {
        if(StringUtils.isNullOrEmpty(category.toString())) {
            return null
        }

        return when(category) {
            null -> null
            else -> challenge.challengeStatus.stringValue().eq(category.toString())
        }
    }

    private fun checkChallengeStatus(challengeStatus: CHALLENGE_STATUS?): BooleanExpression? {
        if(StringUtils.isNullOrEmpty(challengeStatus.toString())) {
            return null
        }

        return when(challengeStatus) {
            CHALLENGE_STATUS.BEFORE -> challenge.challengeStatus.eq(CHALLENGE_STATUS.BEFORE)
            CHALLENGE_STATUS.INPROGRESS -> challenge.challengeStatus.eq(CHALLENGE_STATUS.INPROGRESS)
            CHALLENGE_STATUS.END -> challenge.challengeStatus.eq(CHALLENGE_STATUS.END)
            else -> null
        }
    }

    private fun getOrderSpecifier(orderType: CHALLENGE_ORDER_TYPE?): OrderSpecifier<*> {
        return when (orderType) {
            CHALLENGE_ORDER_TYPE.POPULAR -> OrderSpecifier(Order.DESC, challenge.achievementRate)
            CHALLENGE_ORDER_TYPE.RECENTYL -> OrderSpecifier(Order.DESC, challenge.lastModifiedDate)
            null -> OrderSpecifier(Order.DESC, challenge.id)
        }
    }
}

package com.project.jiguhada.repository.challenge


import com.project.jiguhada.domain.challenge.Challenge
import com.project.jiguhada.domain.challenge.QChallenge
import com.project.jiguhada.domain.challenge.QChallenge.challenge
import com.project.jiguhada.util.*
import com.querydsl.core.BooleanBuilder
import com.querydsl.core.types.Order
import com.querydsl.core.types.OrderSpecifier
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.core.util.StringUtils
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport

data class ChallengeSupportImpl(
    private val queryFactory: JPAQueryFactory,
    private val challengeTagRepository: ChallengeTagRepository,
    private val tagRepository: TagRepository
): QuerydslRepositorySupport(Challenge::class.java), ChallengeSupport {

        /*private fun isTagExist(tag: CHALLENGE_TAG) : BooleanExpression? {
            return when(tag) {
                null -> null
                else -> return challenge.challengeTags.any().tag.eq(tagRepository.findByChallengeTagName(tag))
            }
        }*/

    override fun findChallengeMainList(
        status: CHALLENGE_STATUS?,
        page: Pageable
    ): List<Challenge> {
        return queryFactory.selectFrom(challenge)
            .where(
                checkChallengeStatus(status)
            )
            .orderBy(OrderSpecifier(Order.DESC, challenge.createdDate))
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()
    }

    override fun findChallengeListsCount(
        query: String?,
        searchType: CHALLENGE_SEARCH_TYPE?,
        challengeOrder: CHALLENGE_ORDER_TYPE?,
        category: CHALLENGE_CATEGORY?,
        status: CHALLENGE_STATUS?,
        tagList: List<CHALLENGE_TAG>?,
        page: Pageable
    ): List<Challenge> {
        return queryFactory.selectFrom(challenge)
            .where(
                isSearchExist(query, searchType),
                checkChallengeStatus(status),
                checkChallengeCategory(category),
                isExistTag(tagList, challenge)
            )
            .fetch()
    }

    private fun isExistTag(tagList: List<CHALLENGE_TAG>?, challenge: QChallenge): BooleanBuilder {
        val builder = BooleanBuilder()
        tagList?.forEach {
            builder.and(challenge.challengeTags.any().tag.challengeTagName.eq(it))
        }
        return builder
    }

    override fun findChallengeLists(
        query: String?,
        searchType: CHALLENGE_SEARCH_TYPE?,
        challengeOrder: CHALLENGE_ORDER_TYPE?,
        category: CHALLENGE_CATEGORY?,
        status: CHALLENGE_STATUS?,
        tagList: List<CHALLENGE_TAG>?,
        page: Pageable
    ): List<Challenge> {

        return queryFactory.selectFrom(challenge)
            .where(
                isSearchExist(query, searchType),
                checkChallengeStatus(status),
                checkChallengeCategory(category),
                /*challenge.challengeTags.any().tag.eqAll(
                    JPAExpressions.selectFrom(tag)
                        .where(
                            tag.challengeTagName.`in`(tagList)
                        )
                )*/
                isExistTag(tagList, challenge)
            )
            .orderBy(
                getOrderSpecifier(challengeOrder)
            )
            .offset(page.offset)
            .limit(page.pageSize.toLong())
            .fetch()
    }

    private fun isSearchExist(query: String?, searchType: CHALLENGE_SEARCH_TYPE?): BooleanExpression? {
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
            else -> challenge.challengeCategory.categoryName.eq(category)
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
            CHALLENGE_ORDER_TYPE.RECENTLY -> OrderSpecifier(Order.DESC, challenge.lastModifiedDate)
            null -> OrderSpecifier(Order.DESC, challenge.id)
        }
    }
}

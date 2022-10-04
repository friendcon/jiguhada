package com.project.jiguhada.repository.challenge


import com.project.jiguhada.domain.challenge.Challenge
import com.project.jiguhada.domain.challenge.QChallenge.challenge
import com.project.jiguhada.util.CHALLENGE_CATEGORY
import com.project.jiguhada.util.CHALLENGE_ORDER_TYPE
import com.project.jiguhada.util.CHALLENGE_SEARCH_TYPE
import com.project.jiguhada.util.CHALLENGE_STATUS
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
    ) {

    }

    private fun isquerySearchExist(query: String?, searchType: CHALLENGE_SEARCH_TYPE?): BooleanExpression? {
        if(StringUtils.isNullOrEmpty(query)) {
            return null
        }

        val tag = tagRepository.findById(query!!)?:null


        return when(searchType) {
            CHALLENGE_SEARCH_TYPE.TITLE -> challenge.title.containsIgnoreCase(query)
            CHALLENGE_SEARCH_TYPE.CONTENT -> challenge.challengeDetails.containsIgnoreCase(query)
            CHALLENGE_SEARCH_TYPE.TAG -> challenge.title.containsIgnoreCase(query)
            else -> null
        }
    }
}

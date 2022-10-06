package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.Challenge
import com.project.jiguhada.util.CHALLENGE_CATEGORY
import com.project.jiguhada.util.CHALLENGE_ORDER_TYPE
import com.project.jiguhada.util.CHALLENGE_SEARCH_TYPE
import com.project.jiguhada.util.CHALLENGE_STATUS
import org.springframework.data.domain.Pageable

interface ChallengeSupport {
    fun findChallengeLists(
        query: String?,
        searchType: CHALLENGE_SEARCH_TYPE?,
        challengeOrder: CHALLENGE_ORDER_TYPE?,
        category: CHALLENGE_CATEGORY?,
        status: CHALLENGE_STATUS?,
        page: Pageable
    ): List<Challenge>

    fun findChallengeListsCount(
        query: String?,
        searchType: CHALLENGE_SEARCH_TYPE?,
        challengeOrder: CHALLENGE_ORDER_TYPE?,
        category: CHALLENGE_CATEGORY?,
        status: CHALLENGE_STATUS?,
        page: Pageable
    ): List<Challenge>
}
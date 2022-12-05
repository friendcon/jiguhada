package com.project.jiguhada.repository.challenge

import com.project.jiguhada.controller.dto.challenge.ChallengeListItem
import com.project.jiguhada.domain.challenge.Challenge
import com.project.jiguhada.util.*
import org.springframework.data.domain.Pageable

interface ChallengeSupport {
    fun findChallengeLists(
        query: String?,
        searchType: CHALLENGE_SEARCH_TYPE?,
        challengeOrder: CHALLENGE_ORDER_TYPE?,
        category: CHALLENGE_CATEGORY?,
        status: CHALLENGE_STATUS?,
        tagList: List<CHALLENGE_TAG>?,
        page: Pageable
    ): List<Challenge>

    fun findChallengeListsCount(
        query: String?,
        searchType: CHALLENGE_SEARCH_TYPE?,
        challengeOrder: CHALLENGE_ORDER_TYPE?,
        category: CHALLENGE_CATEGORY?,
        status: CHALLENGE_STATUS?,
        tagList: List<CHALLENGE_TAG>?,
        page: Pageable
    ): List<Challenge>

    fun findChallengeMainList(
        status: CHALLENGE_STATUS?,
        page: Pageable
    ): List<Challenge>
}
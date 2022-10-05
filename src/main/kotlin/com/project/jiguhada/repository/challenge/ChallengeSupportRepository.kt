package com.project.jiguhada.repository.challenge

import com.project.jiguhada.controller.dto.challenge.ChallengeListItem
import com.project.jiguhada.util.CHALLENGE_CATEGORY
import com.project.jiguhada.util.CHALLENGE_ORDER_TYPE
import com.project.jiguhada.util.CHALLENGE_SEARCH_TYPE
import com.project.jiguhada.util.CHALLENGE_STATUS
import org.springframework.data.domain.Pageable

interface ChallengeSupportRepository {
    fun findChallengeList(
        query: String?,
        searchType: CHALLENGE_SEARCH_TYPE?,
        order: CHALLENGE_ORDER_TYPE?,
        category: CHALLENGE_CATEGORY?,
        status: CHALLENGE_STATUS?,
        page: Pageable
    ): List<ChallengeListItem>
}
package com.project.jiguhada.controller.dto.challenge

import com.querydsl.core.annotations.QueryProjection

data class ChallengeListResponse @QueryProjection constructor(
    val totalChallengeCount: Long,
    val totalPage: Long,
    val currentPage: Long,
    val challengeList: List<ChallengeListItem>
)

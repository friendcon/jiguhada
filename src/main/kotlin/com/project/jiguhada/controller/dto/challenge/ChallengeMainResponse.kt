package com.project.jiguhada.controller.dto.challenge

data class ChallengeMainResponse(
    val totalChallengeCount: Long,
    val totalPage: Long,
    val currentPage: Long,
    val challengeList: List<ChallengeMainItem>
)

package com.project.jiguhada.controller.dto.challenge

data class ChallengeAuthListResponse(
    val totalCount: Long,
    val totalPage: Long,
    val currentPage: Long,
    val challengeAuthList: List<ChallengeAuthItem>
)

package com.project.jiguhada.controller.dto.challenge

data class ChallengeAuthListResponse(
    val totalCount: Long, // 총 인증 수
    val totalPage: Long,
    val currentPage: Long,
    val challengeAuthList: List<ChallengeAuthItem>
)

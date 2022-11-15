package com.project.jiguhada.controller.dto.challenge

import com.project.jiguhada.util.CHALLENTE_IS_JOIN

data class ChallengeJoinResponse(
    val challengeId: Long,
    val userId: Long,
    val joinStatus: CHALLENTE_IS_JOIN,
    val isChallengeMaster: Boolean
)

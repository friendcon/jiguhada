package com.project.jiguhada.controller.dto.challenge

import com.project.jiguhada.util.CHALLENGE_AUTH_IS_APPROVE

data class ChallengeAuthItem(
    val challengeAuthId: Long,
    val userId: Long,
    val username: String,
    val nickname: String,
    val userProfileImgUrl: String,
    val authContent: String,
    val authImgUrl: String,
    val authIsApprove: CHALLENGE_AUTH_IS_APPROVE
)

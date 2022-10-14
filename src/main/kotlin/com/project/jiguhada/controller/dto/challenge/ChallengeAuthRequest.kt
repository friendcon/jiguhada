package com.project.jiguhada.controller.dto.challenge

data class ChallengeAuthRequest(
    val challengeId: Long,
    val content: String,
    val authImgUrl: String
)

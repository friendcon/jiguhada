package com.project.jiguhada.controller.dto.challenge

import com.project.jiguhada.util.CHALLENGE_PERIOD
import com.project.jiguhada.util.CHALLENGE_STATUS
import com.project.jiguhada.util.CHALLENGE_TAG
import com.querydsl.core.annotations.QueryProjection
import java.math.BigDecimal
import java.time.LocalDateTime

data class ChallengeListItem @QueryProjection constructor(
    val challengeId: Long,
    val challengeTitle: String,
    val challengeDetails: String,
    val challengeImgUrl: String,
    val tagList: MutableList<CHALLENGE_TAG>,
    val currentParticipantsCount: Long,
    val participantsCount: Long,
    val challengeStartDate: LocalDateTime,
    val challengePeroid: CHALLENGE_PERIOD,
    val challengeEndDate: LocalDateTime,
    val challengeStatus: CHALLENGE_STATUS,
    val achievementRate: BigDecimal
)

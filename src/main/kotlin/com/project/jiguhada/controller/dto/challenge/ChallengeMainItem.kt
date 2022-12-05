package com.project.jiguhada.controller.dto.challenge

import com.project.jiguhada.util.AUTH_FREQUENCY
import com.project.jiguhada.util.CHALLENGE_PERIOD
import com.project.jiguhada.util.CHALLENGE_STATUS
import com.project.jiguhada.util.CHALLENGE_TAG
import java.math.BigDecimal
import java.time.LocalDateTime

data class ChallengeMainItem(
    val challengeId: Long,
    val challengeTitle: String,
    val challengeDetails: String,
    val challengeImgUrl: String,
    val currentParticipantsCount: Long,
    val participantsCount: Long,
    val challengeTagList: List<CHALLENGE_TAG>,
    val challengeStartDate: LocalDateTime,
    val challengePeroid: CHALLENGE_PERIOD,
    val challengeEndDate: LocalDateTime,
    val challengeStatus: CHALLENGE_STATUS,
    val authFrequency: AUTH_FREQUENCY,
    val achievementRate: BigDecimal

)

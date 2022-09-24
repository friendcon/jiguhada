package com.project.jiguhada.controller.dto.challenge

import com.project.jiguhada.util.AUTH_AVAILABLE_TIME_TYPE
import com.project.jiguhada.util.AUTH_FREQUENCY
import com.project.jiguhada.util.CHALLENGE_PERIOD
import com.project.jiguhada.util.CHALLENGE_STATUS
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalTime

data class ChallengeCreateResponse(
    val challengeId: Long,
    val challengeTitle: String,
    val challengeDetails: String,
    val participantsCount: Long, // 총 참가자 수
    val currrentParticipantsCount: Long, // 현재 참가자 수
    val challengeTag: List<String>,
    val challengeImg: String,
    val authMethodContent: String,
    val authMethodImgUrl: String?,
    val challengeStartDate: LocalDate, // 챌린지 시작일
    val challengeEndDate: LocalDate, // 챌린지 종료일
    val challengePeroid: CHALLENGE_PERIOD, // 챌린지 기간
    val authFrequency: AUTH_FREQUENCY, // 인증 빈도
    val authAvailableTimeType: AUTH_AVAILABLE_TIME_TYPE, // 인증 가능 시간 타입 (24시간, 지정한 시간)
    val authAvailableStartTime: LocalTime, // 인증 가능 시작 시간
    val authAvailableEndTime: LocalTime, // 인증 가능 종료 시간
    val isOfficial: Boolean, // 공식 챌린지 여부
    val challengeStatus: CHALLENGE_STATUS, // 챌린지 상태
    val achievementRate: BigDecimal // 챌린지 달성률
) {

}

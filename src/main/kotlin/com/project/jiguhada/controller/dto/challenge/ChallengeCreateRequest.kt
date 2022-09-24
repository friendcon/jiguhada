package com.project.jiguhada.controller.dto.challenge

import com.project.jiguhada.util.AUTH_AVAILABLE_TIME_TYPE
import com.project.jiguhada.util.AUTH_FREQUENCY
import com.project.jiguhada.util.CHALLENGE_PERIOD
import com.project.jiguhada.util.CHALLENGE_TAG
import java.time.LocalDate
import java.time.LocalTime

data class ChallengeCreateRequest(
    val title: String,
    val challengeDetails: String, // 챌린지 간단한 설명
    val participantsCount: Long, // 참가자 수
    val challengeTag: List<ChallengeTagRequest>,
    val challengeImg: String, // 챌린지 대표 이미지
    val authMethodContent: String, // 인증방법 설명
    val challengeStartDate: LocalDate, // 챌린지 시작일
    val challengePeroid: CHALLENGE_PERIOD, // 챌린지 기간 (1주, 2주, 3주, 4주)
    val authFrequency: AUTH_FREQUENCY, // 인증빈도
    val authCountPerDay: Long, // 하루 인증 횟수
    val authAvailableTimeType: AUTH_AVAILABLE_TIME_TYPE, // 인증 가능 시간 타입 (24시간, 지정한 시간)
    val authAvailableStartTime: LocalTime, // 인증 가능 시작 시간
    val authAvailableEndTime: LocalTime, // 인증 가능 종료 시간
    val authHoliday: Boolean // 공휴일 인증 여부 true : 공휴일에도 인증 false : 공휴일에는 인증 x
) {

    data class ChallengeTagRequest(
        val tagname: CHALLENGE_TAG
    )
}

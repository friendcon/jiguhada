package com.project.jiguhada.controller.dto.challenge

import com.project.jiguhada.util.*
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime

data class ChallengeCreateResponse(
    val challengeCategory: CHALLENGE_CATEGORY, // 챌린지 카테고리
    val challengeId: Long,
    val challengeTag: List<String>,
    val challengeTitle: String,
    // val challengeDetails: String,
    val challengeImg: String, // 챌린지 대표 이미지
    val challengeAddDetails: String, // 챌린지 추가 설명
    val challengeAddImgs: String, // 챌린지 추가 ㅇ미ㅣ지
    val challengeManagerId: Long, // user id
    val challengeManagerName: String, // user 닉네임
    val challengeManagerImgUrl: String, // user img url
    val participantsCount: Long, // 총 참가자 수
    val currrentParticipantsCount: Long, // 현재 참가자 수
    val authMethodContent: String, // 인증방법 설명
    val authMethodImgUrl: String?, // 인증 성공 이미지
    val authMethodFailImgUrl: String, // 인증 실패 이미지
    val challengeStartDate: LocalDateTime, // 챌린지 시작일
    val challengePeroid: CHALLENGE_PERIOD, // 챌린지 기간
    val challengeEndDate: LocalDateTime, // 챌린지 종료일
    val authFrequency: AUTH_FREQUENCY, // 인증 빈도
    val authCountPerDay: Long, // 하루 인증 횟수
    val authAvailableTimeType: AUTH_AVAILABLE_TIME_TYPE, // 인증 가능 시간 타입 (24시간, 지정한 시간)
    val authAvailableStartTime: LocalTime, // 인증 가능 시작 시간
    val authAvailableEndTime: LocalTime, // 인증 가능 종료 시간
   //  val authHoliday: Boolean, // 공휴일 인증 여부
    val isOfficial: Boolean, // 공식 챌린지 여부
    val challengeStatus: CHALLENGE_STATUS, // 챌린지 상태
    val achievementRate: BigDecimal // 챌린지 달성률
) {

}

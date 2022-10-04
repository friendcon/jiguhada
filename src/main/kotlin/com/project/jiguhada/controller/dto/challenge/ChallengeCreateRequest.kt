package com.project.jiguhada.controller.dto.challenge

import com.project.jiguhada.util.*
import java.time.LocalDate
import java.time.LocalTime

data class ChallengeCreateRequest(
    val challengeTag: List<CHALLENGE_TAG>,
    val title: String, // 챌린지 제목
    val challengeDetails: String, // 챌린지 간단한 설명
    val challengeCategory: CHALLENGE_CATEGORY, // 챌린지 카테고리
    val challengeImg: String, // 챌린지 대표 이미지
    val challengeAddDetails: String, // 챌린지 추가 설명
    val challengeAddImg: String , // 챌린지 추가 이미지
    val participantsCount: Long, // 참가자 수
    val authMethodContent: String, // 인증방법 설명
    val authMethodImg: String, // 인증 성공 이미지
    val authMethodFailImg: String, // 인증 실패 이미지
    val challengeStartDate: LocalDate, // 챌린지 시작일
    val challengePeroid: CHALLENGE_PERIOD, // 챌린지 기간 (1주, 2주, 3주, 4주)
    val challengeEndDate: LocalDate, // 챌린지 종료일
    val authFrequency: AUTH_FREQUENCY, // 인증빈도
    val authCountPerDay: Long, // 하루 인증 횟수
    val authAvailableTimeType: AUTH_AVAILABLE_TIME_TYPE, // 인증 가능 시간 타입 (24시간, 지정한 시간)
    val authAvailableStartTime: LocalTime, // 인증 가능 시작 시간
    val authAvailableEndTime: LocalTime, // 인증 가능 종료 시간
    val authHoliday: Boolean // 공휴일 인증 여부 true : 공휴일에도 인증 false : 공휴일에는 인증
) {
}

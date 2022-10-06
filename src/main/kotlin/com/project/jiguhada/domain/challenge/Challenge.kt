package com.project.jiguhada.domain.challenge

import com.project.jiguhada.controller.dto.challenge.ChallengeCreateResponse
import com.project.jiguhada.controller.dto.challenge.ChallengeListItem
import com.project.jiguhada.domain.base.BaseEntity
import com.project.jiguhada.domain.user.UserEntity
import com.project.jiguhada.util.AUTH_AVAILABLE_TIME_TYPE
import com.project.jiguhada.util.AUTH_FREQUENCY
import com.project.jiguhada.util.CHALLENGE_PERIOD
import com.project.jiguhada.util.CHALLENGE_STATUS
import org.hibernate.Hibernate
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.LocalTime
import javax.persistence.*

@Entity
data class Challenge(
    @ManyToOne
    @JoinColumn(name = "challenge_category_category_name")
    val challengeCategory: ChallengeCategory, // 챌린지 카테고리
    @OneToMany(orphanRemoval = true, cascade = [CascadeType.PERSIST])
    @JoinColumn(name = "challenge_ID")
    val challengeTags: List<ChallengeTag> = mutableListOf(), // 챌린지 태그
    val title: String, // 챌린지 제목
    val challengeDetails: String, // 챌린지 설명
    val challengeImg: String, // 챌린지 대표 사진
    val challengeAddDetails: String, // 챌린지 추가 설명
    val challengeAddImg: String, // 챌린지 추가 이미지
    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    val userEntity: UserEntity, // 챌린지 만든사람
    val participantsCount: Long, // 챌린지 참가자 수
    var currrentParticipantsCount: Long, // 현재 참가자 수
    val authMethodContent: String, // 인증 방법 설명
    val authMethodImgUrl: String?, // 인증 성공 이미지
    val authMethodFailImg: String, // 인증 실패 이미지
    val challengeStartDate: LocalDateTime, // 챌린지 시작일
    @Enumerated(EnumType.STRING)
    val challengePeroid: CHALLENGE_PERIOD, // 챌린지 기간
    val challengeEndDate: LocalDateTime, // 챌린지 종료일
    @Enumerated(EnumType.STRING)
    val authFrequency: AUTH_FREQUENCY, // 인증 빈도
    val authCountPerDay: Long, // 하루 인증 횟수
    @Enumerated(EnumType.STRING)
    val authAvailableTimeType: AUTH_AVAILABLE_TIME_TYPE, // 인증 가능 시간 타입
    val authAvailableStartTime: LocalTime, // 인증 가능 시작 시간
    val authAvailableEndTime: LocalTime, // 인증 가능 종료 시간
    val authHoliday: Boolean, // 공휴일 인증 여부
    val isOfficial: Boolean, // 공식 챌린지 여부
    @Enumerated(EnumType.STRING)
    var challengeStatus: CHALLENGE_STATUS, // 챌린지 상태 (시작 전, 진행중, 종료)
    @Column(precision = 5, scale = 2)
    val achievementRate: BigDecimal // 챌린지 전체 달성률
): BaseEntity() {
    fun updateParticipantsCount(): Challenge {
        currrentParticipantsCount++
        return this
    }

    fun updateChallengeStatus(challengeStatus: CHALLENGE_STATUS): Challenge {
        this.challengeStatus = challengeStatus
        return this
    }

    fun toChallengeListItemResponse(): ChallengeListItem {
        return ChallengeListItem(
            challengeId = id!!,
            challengeTitle = title,
            challengeDetails = challengeDetails,
            challengeImgUrl = challengeImg,
            currentParticipantsCount = currrentParticipantsCount,
            participantsCount = participantsCount,
            challengeTagList = challengeTags.map { it.tag.challengeTagName },
            challengeStartDate = challengeStartDate,
            challengePeroid = challengePeroid,
            challengeEndDate = challengeEndDate,
            challengeStatus = challengeStatus,
            achievementRate = achievementRate
        )
    }
    fun toChallengeCreateResponse(): ChallengeCreateResponse {
        return ChallengeCreateResponse(
            challengeId = id!!,
            challengeTag = challengeTags.map { it.tag.challengeTagName.toString() },
            challengeTitle = title,
            challengeDetails = challengeDetails,
            challengeCategory = challengeCategory.categoryName,
            challengeImg = challengeImg,
            challengeAddDetails = challengeAddDetails,
            challengeAddImgs = challengeAddImg,
            challengeManagerId = userEntity.id!!,
            challengeManagerName = userEntity.nickname,
            challengeManagerImgUrl = userEntity.userImageUrl,
            participantsCount = participantsCount,
            currrentParticipantsCount = currrentParticipantsCount,
            authMethodContent = authMethodContent,
            authMethodImgUrl = authMethodImgUrl,
            authMethodFailImgUrl = authMethodFailImg,
            challengeStartDate = challengeStartDate,
            challengePeroid = challengePeroid,
            challengeEndDate = challengeEndDate,
            authFrequency = authFrequency,
            authCountPerDay = authCountPerDay,
            authAvailableTimeType = authAvailableTimeType,
            authAvailableStartTime = authAvailableStartTime,
            authAvailableEndTime = authAvailableEndTime,
            authHoliday = authHoliday,
            isOfficial = isOfficial,
            challengeStatus = challengeStatus,
            achievementRate = achievementRate
        )
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Challenge

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String {
        return "Challenge(challengeTags=$challengeTags, title='$title', challengeDetails='$challengeDetails', challengeImg='$challengeImg', challengeAddDetails='$challengeAddDetails', challengeAddImg='$challengeAddImg', userEntity=$userEntity, participantsCount=$participantsCount, currrentParticipantsCount=$currrentParticipantsCount, authMethodContent='$authMethodContent', authMethodImgUrl=$authMethodImgUrl, authMethodFailImg='$authMethodFailImg', challengeStartDate=$challengeStartDate, challengePeroid=$challengePeroid, challengeEndDate=$challengeEndDate, authFrequency=$authFrequency, authCountPerDay=$authCountPerDay, authAvailableTimeType=$authAvailableTimeType, authAvailableStartTime=$authAvailableStartTime, authAvailableEndTime=$authAvailableEndTime, authHoliday=$authHoliday, isOfficial=$isOfficial, challengeStatus=$challengeStatus, achievementRate=$achievementRate)"
    }


}

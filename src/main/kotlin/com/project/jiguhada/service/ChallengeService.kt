package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.challenge.ChallengeCreateRequest
import com.project.jiguhada.controller.dto.challenge.ChallengeCreateResponse
import com.project.jiguhada.controller.dto.challenge.ChallengeTagRequest
import com.project.jiguhada.domain.challenge.Challenge
import com.project.jiguhada.domain.challenge.ChallengeTag
import com.project.jiguhada.domain.challenge.UserChallenge
import com.project.jiguhada.repository.challenge.ChallengeRepository
import com.project.jiguhada.repository.challenge.ChallengeTagRepository
import com.project.jiguhada.repository.challenge.UserChallengeRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.CHALLENGE_PERIOD
import com.project.jiguhada.util.CHALLENGE_STATUS
import com.project.jiguhada.util.SecurityUtil
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Service
class ChallengeService(
    private val userEntityRepository: UserEntityRepository,
    private val challengeRepository: ChallengeRepository,
    private val challengeTagRepository: ChallengeTagRepository,
    private val userChallengeRepository: UserChallengeRepository
) {

    @Transactional
    fun createChallenge(challengeCreateRequest: ChallengeCreateRequest): ChallengeCreateResponse {
        val challenge = challengeRepository.save(challengeCreateRequest.toEntity())
        val user = userEntityRepository.findByUsername(SecurityUtil.currentUsername)
        val userChallenge = UserChallenge(
            userEntity = user.get(),
            challenge = challenge,
            achievementRate = BigDecimal.valueOf(0.00)
        )
        userChallengeRepository.save(userChallenge)
        return challenge.toChallengeCreateResponse()
    }

    fun ChallengeCreateRequest.toEntity(): Challenge {
        val user = userEntityRepository.findByUsername(SecurityUtil.currentUsername).get()

        return Challenge(
            title = title,
            userEntity = user,
            challengeDetails = challengeDetails,
            participantsCount = participantsCount,
            currrentParticipantsCount = 1,
            challengeTags = challengeTag.map { it.toEntity() },
            challengeImg = challengeImg,
            authMethodContent = authMethodContent,
            authMethodImgUrl = authMethodImg,
            challengeStartDate = challengeStartDate,
            challengeEndDate = calculateChallengeEndDate(challengeStartDate, challengePeroid),
            challengePeroid = challengePeroid,
            authFrequency = authFrequency,
            authCountPerDay = authCountPerDay,
            authAvailableTimeType = authAvailableTimeType,
            authAvailableStartTime = authAvailableStartTime,
            authAvailableEndTime = authAvailableEndTime,
            authHoliday = authHoliday,
            isOfficial = false,
            challengeStatus = CHALLENGE_STATUS.BEFORE,
            achievementRate = BigDecimal.valueOf(0.00)
        )
    }

    fun calculateChallengeEndDate(startDate: LocalDate, challengePeriod: CHALLENGE_PERIOD): LocalDate {
        return when(challengePeriod) {
            CHALLENGE_PERIOD.ONEWEEK -> startDate.plusWeeks(1)
            CHALLENGE_PERIOD.TWOWEEK -> startDate.plusWeeks(2)
            CHALLENGE_PERIOD.THREEWEEK -> startDate.plusWeeks(3)
            CHALLENGE_PERIOD.FOURWEEK -> startDate.plusWeeks(4)
        }
    }

    fun ChallengeTagRequest.toEntity(): ChallengeTag {
        return challengeTagRepository.findById(tagname.toString()).get()
    }
}
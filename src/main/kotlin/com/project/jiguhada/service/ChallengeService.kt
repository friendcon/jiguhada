package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.challenge.ChallengeCreateRequest
import com.project.jiguhada.controller.dto.challenge.ChallengeCreateResponse
import com.project.jiguhada.controller.dto.challenge.ChallengeJoinRequest
import com.project.jiguhada.domain.challenge.Challenge
import com.project.jiguhada.domain.challenge.ChallengeTag
import com.project.jiguhada.domain.challenge.UserChallenge
import com.project.jiguhada.exception.ChallengeJoinCountException
import com.project.jiguhada.exception.UserAlreadyChallengeMemberException
import com.project.jiguhada.repository.challenge.ChallengeRepository
import com.project.jiguhada.repository.challenge.ChallengeTagRepository
import com.project.jiguhada.repository.challenge.TagRepository
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
    private val userChallengeRepository: UserChallengeRepository,
    private val tagRepository: TagRepository
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

    @Transactional
    fun joinChallenge(challengeJoinRequest: ChallengeJoinRequest) {
        val userList = userChallengeRepository.findByChallengeId(challengeJoinRequest.challengeId).map { it.userEntity.id }
        val challenge = challengeRepository.findById(challengeJoinRequest.challengeId).get()
        val user = userEntityRepository.findByUsername(SecurityUtil.currentUsername).get()

        if(challenge.currrentParticipantsCount == challenge.participantsCount) {
            throw ChallengeJoinCountException("정원이 초과되어 챌린지에 참여할 수 없습니다.")
        } else if(userList.contains(challengeJoinRequest.userId)) {
            throw UserAlreadyChallengeMemberException("이미 챌린지 참가자입니다.")
        }

        val userChallenge = UserChallenge(
            user,
            challenge,
            BigDecimal.valueOf(0.00)
        )

        userChallengeRepository.save(userChallenge)
        challenge.updateParticipantsCount()
    }

    fun ChallengeCreateRequest.toEntity(): Challenge {
        val user = userEntityRepository.findByUsername(SecurityUtil.currentUsername).get()

        return Challenge(
            title = title,
            userEntity = user,
            challengeDetails = challengeDetails,
            participantsCount = participantsCount,
            currrentParticipantsCount = 1,
            challengeTags = challengeTag.map { ChallengeTag(tag = tagRepository.findByChallengeTagName(it)) },
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
}
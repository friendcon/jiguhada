package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.CommonResponseDto
import com.project.jiguhada.controller.dto.challenge.ChallengeAuthRequest
import com.project.jiguhada.domain.challenge.Challenge
import com.project.jiguhada.domain.challenge.ChallengeAuth
import com.project.jiguhada.domain.user.UserEntity
import com.project.jiguhada.exception.AlreadyAuthException
import com.project.jiguhada.exception.CantAuthException
import com.project.jiguhada.exception.ChallengeException
import com.project.jiguhada.exception.UnauthorizedRequestException
import com.project.jiguhada.repository.challenge.ChallengeAuthRepository
import com.project.jiguhada.repository.challenge.ChallengeRepository
import com.project.jiguhada.repository.challenge.UserChallengeRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.*
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.time.LocalDate

@Service
class ChallengeAuthService(
    private val userEntityRepository: UserEntityRepository,
    private val userChallengeRepository: UserChallengeRepository,
    private val challengeRepository: ChallengeRepository,
    private val challengeAuthRepository: ChallengeAuthRepository
) {

    /**
     * 인증 생성
     * 0. 챌린지 인증하기 전 챌린지에 참여하는 사람인지 확인
     * 0.1 챌린지 인증 기간이 지났는지 확인
     * 0.1.1 인증에 성공한 챌린지가 있는지 확인
     * 0.2 오늘이 챌린지 인증 날짜에 해당하는지.. 확인 ? -> 인증해야할 날짜가 아니라면 인증글을 못올리도록 해야한다
     * 1. 인증 정보 저장
     * - 달성률은 인증이 승인될 때 변경해야한다
     */
    @Transactional
    fun createChallengeAuth(challengeAuthRequest: ChallengeAuthRequest) {
        val today = LocalDate.now()
        val user = userEntityRepository.findByUsername(SecurityUtil.currentUsername).get()
        val challenge = challengeRepository.findById(challengeAuthRequest.challengeId).get()
        val challengeEndDate = challenge.challengeEndDate.toLocalDate()
        val challengeParticipateList = userChallengeRepository.findByChallengeId(challengeAuthRequest.challengeId).map { it.userEntity.username }
        val challengeAuthType = challenge.authFrequency

        if(!challengeParticipateList.contains(SecurityUtil.currentUsername)) {
            throw UnauthorizedRequestException("챌린지 인증 권한이 없습니다")
        } else if(today.isAfter(challengeEndDate)) {
            throw ChallengeException("종료된 챌린지 입니다")
        } else if(challengeAuthRepository.findSuccessAuthByDateAndUserId(today, user.id!!) != null) {
            throw CantAuthException("이미 인증을 한 챌린지 입니다")
        } else if(challengeAuthRepository.findWaitAuthByDateAndUserId(today, user.id!!) != null) {
            throw AlreadyAuthException("승인 대기중인 인증 내역이 있습니다")
        }

        // 실패한 인증 내역이 있다면 인증을 해야함
        val challengeAuth = challengeAuthRequest.toEntity(user)
        challengeAuthRepository.save(challengeAuth)
    }

    // 챌린지 인증 리스트 내림차순으로 정렬
    @Transactional
    fun readChallengeAuthList(challengeId: Long, pageable: Pageable) {
        val response = challengeAuthRepository.findChallengeAuthList(challengeId, pageable).map { it.toAuthItemResponse() }
    }

    /**
     * 인증 승인 거절
     */
    @Transactional
    fun refuseChallengeAuth(challengeAuthId: Long): CommonResponseDto {
        val challengeAuth = challengeAuthRepository.findById(challengeAuthId).get()
        challengeAuth.refuseChallengeAuth()
        challengeAuth.updateAuthStatus(CHALLENGE_AUTH_STATUS.FAIL)
        return CommonResponseDto(
            200,
            "승인 거절"
        )
    }

    /**
     * 인증 승인
     * 0. 인증 승인 요청하는 username과 챌린지 주인의 username 이 일치하면 인증 승인할 수 있도록 하기
     * 1. 인증 승인 상태 변경 authIsApprove : WAIT -> APPROVE , authStatus : WAIT -> SUCCESS
     * 2. 개인 챌린지 달성률 변경 (인증에 성공했으니.. 달성률을 변경해줘야한다)
     * 3. 전체 챌린지 달성률 변경 (산술평균으로 계산)
     */
    @Transactional
    fun approveChallengeAuth(challengeAuthId: Long): CommonResponseDto {
        val challenge = challengeRepository.findById(challengeAuthId).get()
        val challengeOwner = challenge.userEntity.username
        val challengeAuth = challengeAuthRepository.findById(challengeAuthId).get() // 승인할 인증

        // 1. 인증 승인 요청 user 비교
        if(challengeOwner != SecurityUtil.currentUsername) {
            throw UnauthorizedRequestException("권한이 없는 요청입니다")
        }

        // 2. 인증 승인 상태 변경
        challengeAuth.approveChallengeAuth()
        challengeAuth.updateAuthStatus(CHALLENGE_AUTH_STATUS.SUCCESS)

        // 3. 개인 챌린지 달성률 변경
        calcChallengeAchieveRate(challengeAuth.userEntity.id!!, challenge)

        // 4. 전체 챌린지 달성률의 평균 변경
        var  totalAchievement = BigDecimal.valueOf(0L).setScale(2)
        userChallengeRepository.findByChallengeId(challenge.id!!).forEach{
            totalAchievement += it.achievementRate
        }

        totalAchievement.divide(BigDecimal.valueOf(challenge.currrentParticipantsCount))

        challenge.updateChallengeAchievementRate(totalAchievement)

        return CommonResponseDto(
            200,
            "승인 완료"
        )
    }

    // 사용자 한명의 달성률 변경
    @Transactional
    fun calcChallengeAchieveRate(userId: Long, challenge: Challenge) {
        // 전체 인증 수
        val challengeAuthListCount = challengeAuthRepository
            .countByUserEntity_IdAndChallenge_IdAndAndAuthIsApprove(userId, challenge.id!!, CHALLENGE_AUTH_STATUS.SUCCESS).toBigDecimal()

        // 현재 인증 수
        val challengeTotalAuthCount = (getChallengeAuthFrequencyValue(challenge.authFrequency)*getChallengePeriodValue(challenge.challengePeroid)).toBigDecimal()
        val userChallenge = userChallengeRepository.findByUserEntity_IdAndChallenge_Id(userId, challenge.id!!)
        val achieveRate = challengeAuthListCount.divide(challengeTotalAuthCount).multiply(BigDecimal.valueOf(100L)).setScale(2)
        userChallenge.updateAchievementRate(achieveRate) // 업데이트
    }

    fun getChallengePeriodValue(challengePeriod: CHALLENGE_PERIOD) : Long {
        return when(challengePeriod) {
            CHALLENGE_PERIOD.ONEWEEK -> 1L
            CHALLENGE_PERIOD.TWOWEEK -> 2L
            CHALLENGE_PERIOD.THREEWEEK -> 3L
            CHALLENGE_PERIOD.FOURWEEK -> 4L
        }
    }

    fun getChallengeAuthFrequencyValue(challengeFrequency: AUTH_FREQUENCY): Long {
        return when(challengeFrequency) {
            AUTH_FREQUENCY.EVERYDAY -> 7L
            AUTH_FREQUENCY.WEEKDAY -> 5L
            AUTH_FREQUENCY.WEEKEND -> 2L
            AUTH_FREQUENCY.ONCEAWEEK -> 1L
            AUTH_FREQUENCY.TWICEAWEEK -> 2L
            AUTH_FREQUENCY.THIRDAWEEK -> 3L
            AUTH_FREQUENCY.FORTHAWEEK -> 4L
            AUTH_FREQUENCY.FIFTHAWEEK -> 5L
            AUTH_FREQUENCY.SIXTHAWEEK -> 6L
        }
    }

    fun ChallengeAuthRequest.toEntity(user: UserEntity): ChallengeAuth {
        val challenge = challengeRepository.findById(challengeId).get()
        return ChallengeAuth(
            challenge = challenge,
            userEntity = user,
            authDate = LocalDate.now(),
            authImgUrl = authImgUrl,
            content = content,
            authStatus = CHALLENGE_AUTH_STATUS.WAIT,
            authIsApprove = CHALLENGE_AUTH_IS_APPROVE.WAIT
        )
    }
}
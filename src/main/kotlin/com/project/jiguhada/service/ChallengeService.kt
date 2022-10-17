package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.challenge.*
import com.project.jiguhada.controller.dto.user.ImgUrlResponseDto
import com.project.jiguhada.domain.challenge.Challenge
import com.project.jiguhada.domain.challenge.ChallengeTag
import com.project.jiguhada.domain.challenge.UserChallenge
import com.project.jiguhada.exception.ChallengeJoinCountException
import com.project.jiguhada.exception.ChallengeJoinEndException
import com.project.jiguhada.exception.UserAlreadyChallengeMemberException
import com.project.jiguhada.repository.challenge.ChallengeCategoryRepository
import com.project.jiguhada.repository.challenge.ChallengeRepository
import com.project.jiguhada.repository.challenge.TagRepository
import com.project.jiguhada.repository.challenge.UserChallengeRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.*
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.multipart.MultipartFile
import java.math.BigDecimal
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class ChallengeService(
    private val userEntityRepository: UserEntityRepository,
    private val challengeRepository: ChallengeRepository,
    private val challengeCategoryRepository: ChallengeCategoryRepository,
    private val awsS3Service: AwsS3Service,
    private val userChallengeRepository: UserChallengeRepository,
    private val tagRepository: TagRepository
) {
    companion object {
        const val CHALLENGE_PROFILE_DIR_NAME = "challenge-profile-img"
    }
    @Transactional
    fun createChallenge(challengeCreateRequest: ChallengeCreateRequest): ChallengeCreateResponse {
        val challenge = challengeRepository.save(challengeCreateRequest.toEntity())
        println(challenge.toString())

        val user = userEntityRepository.findByUsername(SecurityUtil.currentUsername)
        val userChallenge = UserChallenge(
            userEntity = user.get(),
            challenge = challenge,
            achievementRate = BigDecimal.valueOf(0.00)
        )

        userChallengeRepository.save(userChallenge)

        return challengeRepository.findById(challenge.id!!).get().toChallengeCreateResponse()
    }

    @Transactional
    fun uploadChallengeImg(multipartFile: MultipartFile): ImgUrlResponseDto {
        val url = awsS3Service.uploadImgToDir(multipartFile, CHALLENGE_PROFILE_DIR_NAME)
        return ImgUrlResponseDto(url)
    }


    // 챌린지 참여
    @Transactional
    fun joinChallenge(challengeJoinRequest: ChallengeJoinRequest): ChallengeCreateResponse {
        val userList = userChallengeRepository.findByChallengeId(challengeJoinRequest.challengeId).map { it.userEntity.id }
        val challenge = challengeRepository.findById(challengeJoinRequest.challengeId).get()
        val user = userEntityRepository.findByUsername(SecurityUtil.currentUsername).get()

        if(LocalDateTime.now().isAfter(challenge.challengeStartDate) ||
            LocalDateTime.now().isEqual(challenge.challengeStartDate)) {
            throw ChallengeJoinEndException("챌린지 모집기간이 지났습니다.")
        } else if(challenge.currrentParticipantsCount == challenge.participantsCount) {
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
        return challenge.toChallengeCreateResponse()
    }

    // 챌린지 정보 조회
    @Transactional
    fun readChallenge(challengeId: Long): ChallengeCreateResponse {
        return challengeRepository.findById(challengeId).get().toChallengeCreateResponse()
    }

    @Transactional
    fun readChallengeList(
        query: String?,
        searchType: CHALLENGE_SEARCH_TYPE?,
        orderType: CHALLENGE_ORDER_TYPE?,
        category: CHALLENGE_CATEGORY?,
        status: CHALLENGE_STATUS?,
        page: Pageable
    ): ChallengeListResponse {
        val totalCount = challengeRepository.findChallengeListsCount(query, searchType, orderType, category, status, page).size
        val totalPage = when (totalCount % 20) {
            0 -> totalCount / 20
            else -> totalCount / 20 + 1
        }

        val currentPage = page.pageNumber.toLong().toLong()

        val response = challengeRepository.findChallengeLists(query, searchType, orderType, category, status, page)

        val entityToResponse = response.map { it.toChallengeListItemResponse() }

        return ChallengeListResponse(
            totalPage = totalPage.toLong(),
            totalChallengeCount = totalCount.toLong(),
            currentPage = currentPage + 1,
            challengeList = entityToResponse
        )
    }

    /**
     * 챌린지 상태 변경 : BEFORE -> INPROGRESS
     */
    @Transactional
    fun updateChallengeStatusStart() {
        val localDateTime = LocalDateTime.of(LocalDate.now(), LocalTime.of(0,0,0,0))
        val challengeList = challengeRepository.findChallengeByChallengeStartDate(localDateTime)
        challengeList.map {
            it.updateChallengeStatus(CHALLENGE_STATUS.INPROGRESS)
        }
    }

    /**
     * 챌린지 상태 변경 : INPROGRESS -> END
     */
    @Transactional
    fun updateChallengeStatusEnd() {
        val localDateTime = LocalDateTime.of(LocalDate.now().minusDays(1L), LocalTime.of(23, 59, 59))
        val challengeList = challengeRepository.findChallengeByChallengeEndDate(localDateTime)
        challengeList.map {
            it.updateChallengeStatus(CHALLENGE_STATUS.END)
        }
    }

    /**
     * 챌린지 시작시 유저 인증 value 생성, 유저 챌린지 달성률 0으로 update
     */

    fun ChallengeCreateRequest.toEntity(): Challenge {
        val user = userEntityRepository.findByUsername(SecurityUtil.currentUsername).get()
        val category = challengeCategoryRepository.findById(challengeCategory).get()
        return Challenge(
            challengeTags = challengeTag.map { ChallengeTag(tag = tagRepository.findByChallengeTagName(it)) },
            title = title,
            challengeDetails = challengeDetails,
            challengeImg = challengeImg, // 챌린지 대표 사진
            challengeAddDetails = challengeDetails,
            challengeCategory = category,
            challengeAddImg = challengeAddImg, // 챌린지 추가 이미지
            userEntity = user,
            participantsCount = participantsCount,
            currrentParticipantsCount = 1,
            authMethodContent = authMethodContent,
            authMethodImgUrl = authMethodImg,
            authMethodFailImg = authMethodFailImg,
            challengeStartDate = LocalDateTime.of(challengeStartDate, LocalTime.of(0,0,0)),
            challengePeroid = challengePeroid, // 챌린지 기간
            challengeEndDate = calculateChallengeEndDate(challengeStartDate, challengePeroid),
            authFrequency = authFrequency,
            authCountPerDay = authCountPerDay,
            authAvailableTimeType = authAvailableTimeType,
            authAvailableStartTime = authAvailableStartTime,
            authAvailableEndTime = authAvailableEndTime,
            isOfficial = false,
            challengeStatus = CHALLENGE_STATUS.BEFORE,
            achievementRate = BigDecimal.valueOf(0.00)
        )
    }

    fun calculateChallengeEndDate(startDate: LocalDate, challengePeriod: CHALLENGE_PERIOD): LocalDateTime {
        val endTime = LocalTime.of(23,59,59)
        return when(challengePeriod) {
            CHALLENGE_PERIOD.ONEWEEK -> LocalDateTime.of(startDate, endTime).plusWeeks(1)
            CHALLENGE_PERIOD.TWOWEEK -> LocalDateTime.of(startDate, endTime).plusWeeks(2)
            CHALLENGE_PERIOD.THREEWEEK -> LocalDateTime.of(startDate, endTime).plusWeeks(3)
            CHALLENGE_PERIOD.FOURWEEK -> LocalDateTime.of(startDate, endTime).plusWeeks(4)
        }
    }
}
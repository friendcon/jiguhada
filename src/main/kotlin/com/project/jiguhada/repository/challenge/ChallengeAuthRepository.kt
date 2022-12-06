package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.ChallengeAuth
import com.project.jiguhada.util.CHALLENGE_AUTH_STATUS
import org.springframework.data.jpa.repository.JpaRepository

interface ChallengeAuthRepository: ChallengeAuthSupport, JpaRepository<ChallengeAuth, Long> {
    fun countByUserEntity_IdAndChallenge_IdAndAuthStatus(
        userId: Long, challengeId: Long,
        authStatus: CHALLENGE_AUTH_STATUS
    ): Long
    fun countByChallenge_Id(challengeId: Long): Long

}
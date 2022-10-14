package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.Challenge
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime

interface ChallengeRepository: ChallengeSupport, JpaRepository<Challenge, Long> {
    fun findChallengeByChallengeStartDate(localDateTime: LocalDateTime): List<Challenge>
}
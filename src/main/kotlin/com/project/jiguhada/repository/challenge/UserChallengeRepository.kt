package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.UserChallenge
import org.springframework.data.jpa.repository.JpaRepository

interface UserChallengeRepository: JpaRepository<UserChallenge, Long> {
    fun findByChallengeId(challengeId: Long): List<UserChallenge>
}
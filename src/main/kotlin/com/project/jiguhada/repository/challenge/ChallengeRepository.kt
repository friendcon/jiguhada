package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.Challenge
import org.springframework.data.jpa.repository.JpaRepository

interface ChallengeRepository: ChallengeSupport, JpaRepository<Challenge, Long> {
}
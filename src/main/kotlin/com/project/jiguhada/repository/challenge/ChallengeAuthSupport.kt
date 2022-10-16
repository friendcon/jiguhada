package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.ChallengeAuth
import org.springframework.data.domain.Pageable
import java.time.LocalDate

interface ChallengeAuthSupport {
    fun findSuccessAuthByDateAndUserId(date: LocalDate, userId: Long): ChallengeAuth?
    fun findWaitAuthByDateAndUserId(date: LocalDate, userId: Long): ChallengeAuth?
    fun findChallengeAuthList(challengeId: Long, pageable: Pageable): List<ChallengeAuth>
}

package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.ChallengeAuth
import java.time.LocalDate

interface ChallengeAuthSupport {
    fun findSuccessAuthByDateAndUserId(date: LocalDate, userId: Long): ChallengeAuth?
    fun findWaitAuthByDateAndUserId(date: LocalDate, userId: Long): ChallengeAuth?
}

package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.ChallengeTag
import org.springframework.data.jpa.repository.JpaRepository

interface ChallengeTagRepository: JpaRepository<ChallengeTag, Long> {
}
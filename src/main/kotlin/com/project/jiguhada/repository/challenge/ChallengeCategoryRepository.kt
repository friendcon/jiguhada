package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.ChallengeCategory
import com.project.jiguhada.util.CHALLENGE_CATEGORY
import org.springframework.data.jpa.repository.JpaRepository

interface ChallengeCategoryRepository: JpaRepository<ChallengeCategory, CHALLENGE_CATEGORY> {
}
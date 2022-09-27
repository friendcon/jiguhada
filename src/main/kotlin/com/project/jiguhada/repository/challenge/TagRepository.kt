package com.project.jiguhada.repository.challenge

import com.project.jiguhada.domain.challenge.Tag
import com.project.jiguhada.util.CHALLENGE_TAG
import org.springframework.data.jpa.repository.JpaRepository

interface TagRepository: JpaRepository<Tag, String> {
    fun findByChallengeTagName(tag: CHALLENGE_TAG): Tag
}
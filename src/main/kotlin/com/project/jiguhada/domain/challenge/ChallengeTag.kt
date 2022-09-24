package com.project.jiguhada.domain.challenge

import com.project.jiguhada.util.CHALLENGE_TAG
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
data class ChallengeTag(
    @Id
    @Enumerated(EnumType.STRING)
    val challengeTagName: CHALLENGE_TAG
)

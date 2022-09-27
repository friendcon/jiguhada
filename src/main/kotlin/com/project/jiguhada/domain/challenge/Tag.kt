package com.project.jiguhada.domain.challenge

import com.project.jiguhada.util.CHALLENGE_TAG
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
data class Tag(
    @Id
    @Enumerated(EnumType.STRING)
    val challengeTagName: CHALLENGE_TAG
) {

    override fun toString(): String {
        return "Tag(challengeTagName=$challengeTagName)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Tag

        return challengeTagName != null && challengeTagName == other.challengeTagName
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

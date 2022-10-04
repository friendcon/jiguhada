package com.project.jiguhada.domain.challenge

import com.project.jiguhada.util.CHALLENGE_CATEGORY
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
data class ChallengeCategory(
    @Id
    @Enumerated(EnumType.STRING)
    val categoryName: CHALLENGE_CATEGORY
) {

    override fun toString(): String {
        return "ChallengeCategory(categoryName=$categoryName)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ChallengeCategory

        return categoryName != null && categoryName == other.categoryName
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

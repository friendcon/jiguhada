package com.project.jiguhada.domain.challenge

import org.hibernate.Hibernate
import javax.persistence.*

@Entity
data class ChallengeTag(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long? = null,
    @ManyToOne
    @JoinColumn(name = "tag_challenge_tag_name")
    val tag: Tag
) {

    override fun toString(): String {
        return "ChallengeTag(id=$id, tag=$tag)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ChallengeTag

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

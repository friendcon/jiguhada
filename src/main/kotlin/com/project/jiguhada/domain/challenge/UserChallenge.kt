package com.project.jiguhada.domain.challenge

import com.project.jiguhada.domain.base.BaseEntity
import com.project.jiguhada.domain.user.UserEntity
import org.hibernate.Hibernate
import java.math.BigDecimal
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class UserChallenge(
    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    val userEntity: UserEntity, // 회원 id
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "user_challenge_id")
    val challenges: MutableList<Challenge> = mutableListOf(), // 챌린지 id
    @Column(precision = 5, scale = 2)
    val achievementRate: BigDecimal // 회원의 챌린지 달성률
): BaseEntity() {

    override fun toString(): String {
        return "UserChallenge(userEntity=$userEntity, challenges=$challenges, achievementRate=$achievementRate)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserChallenge

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

package com.project.jiguhada.domain.challenge

import com.project.jiguhada.controller.dto.challenge.ChallengeAuthItem
import com.project.jiguhada.domain.base.BaseEntity
import com.project.jiguhada.domain.user.UserEntity
import com.project.jiguhada.util.CHALLENGE_AUTH_IS_APPROVE
import com.project.jiguhada.util.CHALLENGE_AUTH_STATUS
import org.hibernate.Hibernate
import java.time.LocalDate
import javax.persistence.*

@Entity
data class ChallengeAuth(
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    val challenge: Challenge,
    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    val userEntity: UserEntity,
    val authDate: LocalDate,
    val authImgUrl: String,
    val content: String,
    @Enumerated(EnumType.STRING)
    var authStatus: CHALLENGE_AUTH_STATUS,
    @Enumerated(EnumType.STRING)
    var authIsApprove: CHALLENGE_AUTH_IS_APPROVE
): BaseEntity() {

    fun approveChallengeAuth(): ChallengeAuth {
        this.authIsApprove = CHALLENGE_AUTH_IS_APPROVE.APPROVE
        return this
    }

    fun refuseChallengeAuth(): ChallengeAuth {
        this.authIsApprove = CHALLENGE_AUTH_IS_APPROVE.REFUSE
        return this
    }

    fun updateAuthStatus(authStatus: CHALLENGE_AUTH_STATUS): ChallengeAuth {
        this.authStatus = authStatus
        return this
    }

    fun toAuthItemResponse(): ChallengeAuthItem {
        return ChallengeAuthItem(
            challengeAuthId = id!!,
            userId = userEntity.id!!,
            username = userEntity.username,
            nickname = userEntity.nickname,
            userProfileImgUrl = userEntity.userImageUrl,
            authContent = content,
            authImgUrl = authImgUrl,
            authIsApprove = authIsApprove
        )
    }
    override fun toString(): String {
        return "ChallengeAuth(challenge=$challenge, userEntity=$userEntity, authDate=$authDate, authImgUrl='$authImgUrl', content='$content', authStatus=$authStatus)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as ChallengeAuth

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

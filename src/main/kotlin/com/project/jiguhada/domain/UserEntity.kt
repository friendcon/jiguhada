package com.project.jiguhada.domain

import com.project.jiguhada.util.SocialType
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
data class UserEntity(
    val username: String,
    val password: String,
    val userImageUrl: String,
    @Enumerated(EnumType.STRING)
    val socialType: SocialType
): BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String {
        return "UserEntity(username='$username', password='$password', userImageUrl='$userImageUrl', socialType=$socialType)"
    }
}
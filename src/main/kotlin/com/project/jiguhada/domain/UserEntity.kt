package com.project.jiguhada.domain

import com.project.jiguhada.controller.dto.ReadUserInfoResponseDto
import com.project.jiguhada.util.ROLE
import com.project.jiguhada.util.SocialType
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
data class UserEntity(
    val username: String,
    var nickname: String,
    var password: String,
    var userImageUrl: String,
    @Enumerated(EnumType.STRING)
    var socialType: SocialType,
    @ManyToMany
    @JoinTable(
        name = "user_entity_roles",
        joinColumns = [JoinColumn(name = "user_entity_id")],
        inverseJoinColumns = [JoinColumn(name = "roles_role_name")]
    )
    var roles: MutableSet<Role> = mutableSetOf()
): BaseEntity() {

    fun toReadUserInfoResponse(): ReadUserInfoResponseDto {
        return ReadUserInfoResponseDto(
            username = this.username,
            nickname = this.nickname,
            imgUrl = this.userImageUrl,
            socialType =  this.socialType
        )
    }

    fun updateUserImageUrl(imgUrl: String): String{
        this.userImageUrl = imgUrl
        return imgUrl
    }

    fun updateNickname(nickname: String): String {
        this.nickname = nickname
        return nickname
    }

    fun updatePassword(password: String): String {
        this.password = password
        return password
    }

    fun updateRole(role: ROLE): UserEntity {
        roles.add(Role(role))
        return this
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as UserEntity

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
    override fun toString(): String {
        return "UserEntity(username='$username', nickname='$nickname', password='$password', userImageUrl='$userImageUrl', socialType=$socialType)"
    }



}
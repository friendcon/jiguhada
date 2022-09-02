package com.project.jiguhada.domain

import com.project.jiguhada.util.ROLE
import com.project.jiguhada.util.SocialType
import org.hibernate.Hibernate
import javax.persistence.*

@Entity
data class UserEntity(
    val username: String,
    val nickname: String,
    val password: String,
    val userImageUrl: String,
    @Enumerated(EnumType.STRING)
    val socialType: SocialType,
    @ManyToMany
    @JoinTable(
        name = "user_entity_roles",
        joinColumns = [JoinColumn(name = "user_entity_id")],
        inverseJoinColumns = [JoinColumn(name = "roles_role_name")]
    )
    var roles: MutableSet<Role> = mutableSetOf()
): BaseEntity() {

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
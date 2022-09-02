package com.project.jiguhada.domain

import com.project.jiguhada.util.ROLE
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
data class Role(
    @Id
    @Enumerated(EnumType.STRING)
    val roleName: ROLE
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as Role

        return roleName == other.roleName
    }

    override fun hashCode(): Int = javaClass.hashCode()

    override fun toString(): String {
        return "Role(roleName=$roleName)"
    }
}

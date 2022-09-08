package com.project.jiguhada.domain.board

import com.project.jiguhada.domain.BaseEntity
import com.project.jiguhada.util.BOARD_CATEGORY
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated

@Entity
data class BoardCategory(
    @Enumerated(EnumType.STRING)
    val categoryName: BOARD_CATEGORY,
): BaseEntity() {
    override fun toString(): String {
        return "BoardCategory(categoryName='$categoryName')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BoardCategory

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

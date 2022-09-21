package com.project.jiguhada.domain.board

import com.project.jiguhada.domain.BaseEntity
import com.project.jiguhada.util.BOARD_CATEGORY
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.EnumType
import javax.persistence.Enumerated
import javax.persistence.Id

@Entity
data class BoardCategory(
    @Id
    @Enumerated(EnumType.STRING)
    val categoryName: BOARD_CATEGORY,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BoardCategory

        return categoryName != null && categoryName == other.categoryName
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(categoryName = $categoryName )"
    }

}

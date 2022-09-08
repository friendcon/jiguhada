package com.project.jiguhada.domain.board

import com.project.jiguhada.domain.BaseEntity
import org.hibernate.Hibernate
import javax.persistence.Entity

@Entity
data class BoardImgUrl(
    val imgUrl: String
): BaseEntity() {
    override fun toString(): String {
        return "BoardImgUrl(imgUrl='$imgUrl')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BoardImgUrl

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

package com.project.jiguhada.domain.board

import com.project.jiguhada.domain.BaseEntity
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class BoardImg(
    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: Board,
    val boardImgUrl: String,
    val isDeleted: Boolean
): BaseEntity() {

    override fun toString(): String {
        return "BoardImg(board=$board, boardImgUrl=$boardImgUrl)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BoardImg

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

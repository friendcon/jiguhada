package com.project.jiguhada.domain.board

import com.project.jiguhada.controller.dto.board.BoardImgResponseDto
import com.project.jiguhada.domain.BaseEntity
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class BoardImg(
    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: Board,
    val imgUrl: String,
    var isDeleted: Boolean
): BaseEntity() {

    fun deleteImg(): BoardImg {
        isDeleted = true
        return this
    }

    fun toResponse(): BoardImgResponseDto {
        return BoardImgResponseDto(
            imgId = id!!,
            imgUrl = imgUrl
        )
    }
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BoardImg

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @Override
    override fun toString(): String {
        return this::class.simpleName + "(id = $id )"
    }

}

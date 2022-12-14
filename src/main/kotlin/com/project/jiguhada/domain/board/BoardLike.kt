package com.project.jiguhada.domain.board

import com.project.jiguhada.controller.dto.board.BoardLikeResponseDto
import com.project.jiguhada.domain.BaseEntity
import com.project.jiguhada.domain.user.UserEntity
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class BoardLike(
    var isLike: Int,
    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: Board,
    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    val userEntity: UserEntity,

): BaseEntity() {
    fun createLike(): BoardLike {
        isLike = 1
        return this
    }
    fun deleteLike(): BoardLike {
        isLike = 0
        return this
    }

    fun toResponse(): BoardLikeResponseDto {
        return BoardLikeResponseDto(
            likeId = id!!,
            userId = userEntity.id!!,
            username = userEntity.username,
            nickname = userEntity.nickname,
            userImgUrl = userEntity.userImageUrl
        )
    }

    override fun toString(): String {
        return "BoardLike(isLike=$isLike, board=$board, userEntity=$userEntity)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BoardLike

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

    @ManyToOne
    @JoinColumn(name = "board_comment_id")
    open var boardComment: BoardComment? = null
}

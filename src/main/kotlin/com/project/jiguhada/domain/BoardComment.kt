package com.project.jiguhada.domain

import com.project.jiguhada.controller.dto.board.CommentResponseDto
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class BoardComment(
    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: Board,
    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    val userEntity: UserEntity,
    val content: String,
): BaseEntity() {
    fun toResponse(): CommentResponseDto {
        return CommentResponseDto(
            commentId = id!!,
            username = userEntity.username,
            nickname = userEntity.nickname,
            content = content
        )
    }

    override fun toString(): String {
        return "BoardComment(board=$board, userEntity=$userEntity, content='$content')"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BoardComment

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()
}

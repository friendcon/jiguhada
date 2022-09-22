package com.project.jiguhada.domain.board

import com.project.jiguhada.controller.dto.board.CommentResponseDto
import com.project.jiguhada.controller.dto.board.ReCommentResponseDto
import com.project.jiguhada.controller.dto.boardcomment.CommentUpdateRequestDto
import com.project.jiguhada.controller.dto.boardcomment.CommentUpdateResponseDto
import com.project.jiguhada.domain.BaseEntity
import com.project.jiguhada.domain.user.UserEntity
import org.hibernate.Hibernate
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne
import javax.persistence.OneToMany

@Entity
data class BoardComment(
    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: Board,
    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    val userEntity: UserEntity,
    var content: String,
    @ManyToOne
    @JoinColumn(name = "board_comment_id")
    val boardComment: BoardComment? = null, // 댓글인지
    @OneToMany(orphanRemoval = true)
    @JoinColumn(name = "board_comment_id")
    val boardComments: MutableList<BoardComment> = mutableListOf() // 대댓글
): BaseEntity() {
    fun updateComment(commentUpdateRequestDto: CommentUpdateRequestDto): BoardComment {
        content = commentUpdateRequestDto.content
        return this
    }
    fun toResponse(): CommentResponseDto {
        return CommentResponseDto(
            commentId = id!!,
            username = userEntity.username,
            nickname = userEntity.nickname,
            content = content,
            createdDate = createdDate,
            parentComment = boardComment?.id,
            childComment = boardComments.map { it.toReCommentResponse() }
        )
    }

    fun toReCommentResponse(): ReCommentResponseDto {
        return ReCommentResponseDto(
            parentCommentId = boardComment?.id,
            commentId = id!!,
            username = userEntity.username,
            nickname = userEntity.nickname,
            content = content,
            createdDate = createdDate
        )
    }

    fun toUpdateCommentResponse(): CommentUpdateResponseDto {
        return CommentUpdateResponseDto(
            commentId = id!!,
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

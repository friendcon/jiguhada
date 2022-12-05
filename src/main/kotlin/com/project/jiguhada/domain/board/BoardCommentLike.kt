package com.project.jiguhada.domain.board

import com.project.jiguhada.controller.dto.boardcomment.BoardCommentLikeItem
import com.project.jiguhada.domain.base.BaseEntity
import com.project.jiguhada.domain.user.UserEntity
import org.hibernate.Hibernate
import javax.persistence.Column
import javax.persistence.Entity
import javax.persistence.JoinColumn
import javax.persistence.ManyToOne

@Entity
data class BoardCommentLike(
    var isLike: Long, // 0 좋아요 1 좋아요 취소
    @ManyToOne
    @JoinColumn(name = "board_id")
    val board: Board,
    @ManyToOne
    @JoinColumn(name = "user_entity_id")
    val userEntity: UserEntity,
    @ManyToOne
    @JoinColumn(name = "board_comment_id")
    val boardComment: BoardComment
): BaseEntity() {

    fun createLike(): BoardCommentLike {
        this.isLike = 0L
        return this
    }

    fun deleteLike(): BoardCommentLike {
        this.isLike = 1L
        return this
    }

    fun toResponse(): BoardCommentLikeItem {
        return BoardCommentLikeItem(
            likeId = id!!,
            username = userEntity.username,
            usernickname = userEntity.nickname,
            userId = userEntity.id!!,
            userImgUrl = userEntity.userImageUrl,
            userInfoPublic = userEntity.isUserInfoPublic.toString()
        )
    }

    override fun toString(): String {
        return "BoardCommentLike(isLike=$isLike, board=$board, userEntity=$userEntity)"
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || Hibernate.getClass(this) != Hibernate.getClass(other)) return false
        other as BoardCommentLike

        return id != null && id == other.id
    }

    override fun hashCode(): Int = javaClass.hashCode()

}

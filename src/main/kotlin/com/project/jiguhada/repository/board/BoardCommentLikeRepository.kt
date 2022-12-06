package com.project.jiguhada.repository.board

import com.project.jiguhada.domain.board.BoardCommentLike
import org.springframework.data.jpa.repository.JpaRepository

interface BoardCommentLikeRepository: JpaRepository<BoardCommentLike, Long> {
    fun findByUserEntityIdAndBoardCommentId(userId: Long, commentId: Long): BoardCommentLike?
    fun findByBoardId(boardId: Long): List<BoardCommentLike>
    fun findByWhetherHeartAndBoardId(boardId: Long, isLike: Long): Int
}
package com.project.jiguhada.repository.board

import com.project.jiguhada.domain.board.BoardComment
import org.springframework.data.jpa.repository.JpaRepository

interface BoardCommentRepository: JpaRepository<BoardComment, Long>, BoardCommentSupport {
    fun findByBoardCommentNullOrderByCreatedDateDesc(): List<BoardComment>

    // fun findByBoardCommentNullOrderByCreatedDateDesc()
    fun countByUserEntityId(userId: Long): Long
    fun countByBoard_Id(boardId: Long): Long
}
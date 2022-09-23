package com.project.jiguhada.repository.board

import com.project.jiguhada.domain.board.BoardComment
import org.springframework.data.jpa.repository.JpaRepository

interface BoardCommentRepository: JpaRepository<BoardComment, Long>, BoardCommentSupport {
    fun findByBoardCommentNullOrderByCreatedDateAsc(): List<BoardComment>
    fun countByUserEntityId(userId: Long): Long
}
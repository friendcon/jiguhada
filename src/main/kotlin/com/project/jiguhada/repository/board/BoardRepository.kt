package com.project.jiguhada.repository.board

import com.project.jiguhada.domain.board.Board
import com.project.jiguhada.domain.board.BoardCategory
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long>, BoardRepositorySupport {
    fun countBoardByBoardCategory(category: BoardCategory): Long
}
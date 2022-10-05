package com.project.jiguhada.repository.board

import com.project.jiguhada.controller.dto.board.refactor.BoardLikeItem
import org.springframework.data.domain.Pageable

interface BoardLikeSupport {
    fun findBoardLikeByBoardId(boardId: Long, pageable: Pageable) : List<BoardLikeItem>

    fun findTop10LikeByDateDesc(boardId: Long): List<BoardLikeItem>
}
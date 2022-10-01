package com.project.jiguhada.repository.board

import com.project.jiguhada.controller.dto.boardcomment.BoardCommentItem
import org.springframework.data.domain.Pageable

interface BoardCommentSupport {
    fun findCommentByUserId(userId: Long, page: Pageable): List<BoardCommentItem>
    fun findCommentList(boardId: Long, page: Pageable): List<BoardCommentItem>
}
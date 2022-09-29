package com.project.jiguhada.repository.board

import com.project.jiguhada.domain.board.Board
import com.project.jiguhada.domain.board.BoardLike
import org.springframework.data.jpa.repository.JpaRepository

interface BoardLikeRepository: JpaRepository<BoardLike, Long> {
    fun findByBoard_Id(boardId: Long): List<BoardLike>

    fun findByUserEntity_IdAndBoard_Id(boardId: Long, userId: Long): BoardLike

    fun findByBoard_IdAndUserEntity_Id(boardId: Long, userId: Long): BoardLike
}
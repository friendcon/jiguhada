package com.project.jiguhada.repository.board

import com.project.jiguhada.domain.board.BoardLike
import org.springframework.data.jpa.repository.JpaRepository

interface BoardLikeRepository: JpaRepository<BoardLike, Long> {
}
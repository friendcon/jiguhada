package com.project.jiguhada.repository.board

import com.project.jiguhada.domain.board.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long>, BoardRepositorySupport {
}
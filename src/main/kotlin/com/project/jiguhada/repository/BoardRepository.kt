package com.project.jiguhada.repository

import com.project.jiguhada.domain.Board
import org.springframework.data.jpa.repository.JpaRepository

interface BoardRepository: JpaRepository<Board, Long> {
}
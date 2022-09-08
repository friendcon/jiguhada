package com.project.jiguhada.repository.board

import com.project.jiguhada.domain.board.BoardCategory
import com.project.jiguhada.util.BOARD_CATEGORY
import org.springframework.data.jpa.repository.JpaRepository

interface BoardCategoryRepository: JpaRepository<BoardCategory, Long> {
    fun findByCategoryName(name: BOARD_CATEGORY): BoardCategory
}
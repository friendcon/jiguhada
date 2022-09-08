package com.project.jiguhada.repository

import com.project.jiguhada.domain.BoardCategory
import com.project.jiguhada.util.BOARD_CATEGORY
import org.springframework.data.jpa.repository.JpaRepository

interface BoardCategoryRepository: JpaRepository<BoardCategory, Long> {
    fun findByCategoryName(name: BOARD_CATEGORY): BoardCategory
}
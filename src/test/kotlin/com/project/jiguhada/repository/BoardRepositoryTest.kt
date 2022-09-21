package com.project.jiguhada.repository

import com.project.jiguhada.repository.board.BoardCategoryRepository
import com.project.jiguhada.repository.board.BoardRepository
import com.project.jiguhada.util.BOARD_CATEGORY
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("prod")
class BoardRepositoryTest(
    @Autowired private val boardRepository: BoardRepository,
    @Autowired private val boardCategoryRepository: BoardCategoryRepository
) {
    @Test
    @DisplayName("")
    fun boardRepositoryTest() {
        val category = boardCategoryRepository.findById(BOARD_CATEGORY.FREE).get()
        val result = boardRepository.countBoardByBoardCategory(category)
        println(result)
    }
}

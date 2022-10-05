package com.project.jiguhada.repository

import com.project.jiguhada.repository.board.BoardLikeRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("prod")
class BoardLikeRepository(
    @Autowired private val boardLikeRepository: BoardLikeRepository
) {
    @Test
    @DisplayName("")
    fun BoardLikeRepository() {
        val response = boardLikeRepository.findTop10LikeByDateDesc(97)
        response.forEach {
            println(it.toString())
        }
    }
}
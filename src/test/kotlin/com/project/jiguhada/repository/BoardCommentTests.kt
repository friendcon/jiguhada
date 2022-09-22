package com.project.jiguhada.repository

import com.project.jiguhada.domain.board.BoardComment
import com.project.jiguhada.repository.board.BoardCommentRepository
import com.project.jiguhada.repository.board.BoardRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("prod")
class BoardCommentTests(
    @Autowired private val boardRepository: BoardRepository,
    @Autowired private val userEntityRepository: UserEntityRepository,
    @Autowired private val boardCommentRepository: BoardCommentRepository
) {
    @Test
    @DisplayName("")
    fun commentCreate() {
        val board = boardRepository.findById(48L).get()
        val user = userEntityRepository.findById(5L).get()
        val comment = BoardComment(
            board,
            user,
            "좋은 정보 감사합니당 🐶",
            null,
            mutableListOf()
        )

        val response = boardCommentRepository.save(comment)
        println(response.toString())
    }

    @Test
    @Transactional
    @DisplayName("")
    fun boardReCommentTest() {
        val board = boardRepository.findById(48L).get()
        val user = userEntityRepository.findById(5L).get()
        val parentComment = boardCommentRepository.findById(1L).get()
        val reComment = BoardComment(
            board,
            user,
            "22 저도 감사합니다 🐥",
            parentComment
        )

        boardCommentRepository.save(reComment)

        println(parentComment.toResponse())
    }
}
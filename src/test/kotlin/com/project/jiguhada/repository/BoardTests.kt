package com.project.jiguhada.repository

import com.project.jiguhada.domain.board.Board
import com.project.jiguhada.domain.board.BoardCategory
import com.project.jiguhada.domain.board.BoardComment
import com.project.jiguhada.domain.user.Role
import com.project.jiguhada.domain.user.UserEntity
import com.project.jiguhada.repository.board.BoardCommentRepository
import com.project.jiguhada.repository.board.BoardRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.ROLE
import com.project.jiguhada.util.SocialType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
class BoardTests(
    @Autowired private val boardRepository: BoardRepository,
    @Autowired private val userEntityRepository: UserEntityRepository,
    @Autowired private val boardCommentRepository: BoardCommentRepository
) {

    @Test
    @Transactional
    @DisplayName("")
    fun boardTest() {
        val user = UserEntity(
            username = "d",
            password = "d",
            nickname = "d",
            userImageUrl = "d",
            socialType = SocialType.GENERAL,
            roles = mutableSetOf(Role(ROLE.ROLE_USER))
        )

        val response=userEntityRepository.save(user)

        val board = Board(
            title = "hello",
            content = "hello",
            view_count = 2,
            boardCategory = BoardCategory(BOARD_CATEGORY.FREE),
            userEntity = response
        )

        val save = boardRepository.save(board)
        val comment = BoardComment(
            board,
            user,
            "dd"
        )

        val comment2 = boardCommentRepository.save(comment)

        board.boardCommentsList.add(comment2)

        println(save.toString())
        println(save.boardCommentsList.toString())
        println(save.boardImgs.toString())
        println(save.boardLikes.toString())
    }
}
package com.project.jiguhada.repository

import com.project.jiguhada.domain.board.Board
import com.project.jiguhada.domain.board.BoardCategory
import com.project.jiguhada.domain.board.BoardComment
import com.project.jiguhada.domain.board.BoardLike
import com.project.jiguhada.domain.user.Role
import com.project.jiguhada.domain.user.UserEntity
import com.project.jiguhada.repository.board.BoardCommentRepository
import com.project.jiguhada.repository.board.BoardLikeRepository
import com.project.jiguhada.repository.board.BoardRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.BOARD_ORDER_TYPE
import com.project.jiguhada.util.ROLE
import com.project.jiguhada.util.SocialType
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("prod")
class BoardTests(
    @Autowired private val boardRepository: BoardRepository,
    @Autowired private val userEntityRepository: UserEntityRepository,
    @Autowired private val boardCommentRepository: BoardCommentRepository,
    @Autowired private val boardLikeRepository: BoardLikeRepository
) {

    @Test
    @Transactional
    @DisplayName("")
    fun boardTest() {
        val user = UserEntity(
            username = "ed",
            password = "ed",
            nickname = "ed",
            userImageUrl = "d",
            socialType = SocialType.GENERAL,
            roles = mutableSetOf(Role(ROLE.ROLE_USER))
        )

        val user2 = UserEntity(
            username = "dsd",
            password = "ewq",
            nickname = "dasd",
            userImageUrl = "eqwqew",
            socialType = SocialType.GENERAL,
            roles = mutableSetOf(Role(ROLE.ROLE_USER))
        )

        val response=userEntityRepository.saveAll(listOf(user, user2))

        val board = Board(
            title = "hello",
            content = "hello",
            view_count = 2,
            boardCategory = BoardCategory(BOARD_CATEGORY.FREE),
            userEntity = response[0]
        )
        val board2 = Board(
            title = "hell222o",
            content = "hello",
            view_count = 2,
            boardCategory = BoardCategory(BOARD_CATEGORY.FREE),
            userEntity = response[0]
        )

        val save = boardRepository.save(board)
        val save2 = boardRepository.save(board2)
        val like = BoardLike(
            true,
            board,
            response[1]
        )
        board.boardLikes.add(like)
        // boardLikeRepository.save(like)

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

        val response2 = boardRepository.findBoardList("",BOARD_ORDER_TYPE.RECENT,BOARD_CATEGORY.FREE, PageRequest.of(0, 10))
        response2.forEach {
            println(it.toString())
        }
    }
}
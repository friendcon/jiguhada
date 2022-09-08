package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.board.BoardRequestDto
import com.project.jiguhada.controller.dto.board.BoardResponseDto
import com.project.jiguhada.domain.Board
import com.project.jiguhada.domain.BoardImg
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.BoardCategoryRepository
import com.project.jiguhada.repository.BoardRepository
import com.project.jiguhada.repository.UserEntityRepository
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

@Service
class BoardService(
    private val boardRepository: BoardRepository,
    private val userEntityRepository: UserEntityRepository,
    private val boardCategoryRepository: BoardCategoryRepository,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @Transactional
    fun createBoard(boardRequest: BoardRequestDto, token: String): BoardResponseDto {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)

        val board = boardRequest.toEntity(usernameFromToken)
        val commentToEntity = boardRequest.imgList.map {
            toBoardImgEntity(board, it)
        }

        board.boardImgs = commentToEntity.toMutableSet()
        return board.toResponse()

        throw UsernameNotFoundException("해당 사용자가 존재하지 않습니다.")
    }

    fun resolveToken(token: String): String? {
        return if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }

    fun BoardRequestDto.toEntity(username: String): Board {
        val user = userEntityRepository.findByUsername(username).get()
        val category = boardCategoryRepository.findByCategoryName(category)
        val board = Board(
            title = title,
            content = content,
            boardCategory = category,
            userEntity = user,
            view_count = 0
        )

        return boardRepository.save(board)
    }

    fun toBoardImgEntity(board: Board, imgUrl: String): BoardImg {
        return BoardImg(
            board = board,
            boardImgUrl = imgUrl,
            isDeleted = false
        )
    }
}
package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.board.BoardLikeResponseDto
import com.project.jiguhada.domain.board.BoardLike
import com.project.jiguhada.exception.RequestBoardIdNotMatched
import com.project.jiguhada.exception.UserAlreadyLikeBoard
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.board.BoardLikeRepository
import com.project.jiguhada.repository.board.BoardRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

@Service
class BoardLikeService(
    private val userEntityRepository: UserEntityRepository,
    private val boardRepository: BoardRepository,
    private val boardLikeRepository: BoardLikeRepository,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @Transactional
    fun createLike(boardId: Long, userId: Long, token: String): List<BoardLikeResponseDto> {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        var boardLikeTrueList = boardLikeRepository.findByBoard_Id(boardId).filter { it.isLike == false }.map { it.userEntity.username }

        boardLikeRepository.findByBoard_Id(boardId).forEach {
            println(it.toString())
        }
        var boardLikeFalseList = boardLikeRepository.findByBoard_Id(boardId).filter { it.isLike == true }.map { it.userEntity.username }
        println(boardLikeTrueList.toString())
        println(boardLikeFalseList.toString())
        if(boardLikeTrueList.contains(usernameFromToken)) {
            throw UserAlreadyLikeBoard("이미 좋아요를 누르셨습니다.")
        } else if(boardLikeFalseList.contains(usernameFromToken)) {
            val like = boardLikeRepository.findByBoard_IdAndUserEntity_Id(boardId, userId)
            like.createLike()
        } else {
            val like = BoardLike(
                isLike = true,
                board = boardRepository.findById(boardId).get(),
                userEntity = userEntityRepository.findByUsername(usernameFromToken).get()
            )

            boardLikeRepository.save(like)
        }

        return boardLikeRepository.findByBoard_Id(boardId).filter { it.isLike }.map { it.toResponse() }
    }

    @Transactional
    fun deleteLike(likeId: Long, token: String): List<BoardLikeResponseDto> {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val like = boardLikeRepository.findById(likeId).get()
        if(like.userEntity.username.equals(usernameFromToken)) {
            like.deleteLike()
            return boardLikeRepository.findByBoard_Id(like.board.id!!).filter { it.isLike }.map { it.toResponse() }
        } else {
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
        }
    }

    fun resolveToken(token: String): String? {
        return if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }
}
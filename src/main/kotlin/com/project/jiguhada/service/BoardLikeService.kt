package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.board.BoardLikeResponseDto
import com.project.jiguhada.controller.dto.board.refactor.BoardLikeList
import com.project.jiguhada.domain.board.BoardLike
import com.project.jiguhada.exception.RequestBoardIdNotMatched
import com.project.jiguhada.exception.UserAlreadyLikeBoard
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.board.BoardLikeRepository
import com.project.jiguhada.repository.board.BoardRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import org.springframework.data.domain.Pageable
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
        val boardLikeTrueList = boardLikeRepository.findByBoard_Id(boardId).filter { it.isLike == 1 }.map { it.userEntity.username }

        boardLikeRepository.findByBoard_Id(boardId).forEach {
            println(it.toString())
        }
        val boardLikeFalseList = boardLikeRepository.findByBoard_Id(boardId).filter { it.isLike == 0 }.map { it.userEntity.username }

        if(boardLikeTrueList.contains(usernameFromToken)) {
            throw UserAlreadyLikeBoard("이미 좋아요를 누르셨습니다.")
        } else if(boardLikeFalseList.contains(usernameFromToken)) {
            val likeId = boardLikeRepository.findByBoard_IdAndUserEntity_Id(boardId, userId).id
            val like = boardLikeRepository.findById(likeId!!).get()
            like.createLike()
        } else {
            val like = BoardLike(
                isLike = 1,
                board = boardRepository.findById(boardId).get(),
                userEntity = userEntityRepository.findByUsername(usernameFromToken).get()
            )

            boardLikeRepository.save(like)
        }

        return boardLikeRepository.findByBoard_Id(boardId).filter { it.isLike == 1 }.map { it.toResponse() }
    }

    @Transactional
    fun deleteLike(likeId: Long, token: String): List<BoardLikeResponseDto> {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val like = boardLikeRepository.findById(likeId).get()
        if(like.userEntity.username.equals(usernameFromToken)) {
            like.deleteLike()
            return boardLikeRepository.findByBoard_Id(like.board.id!!).filter { it.isLike == 1 }.map { it.toResponse() }
        } else {
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
        }
    }

    @Transactional
    fun readBoardLikes(
        boardId: Long,
        page: Pageable
    ): BoardLikeList {
        val response = boardLikeRepository.findBoardLikeByBoardId(boardId, page)
        val totalCount = response.size.toLong()
        val totalPage = when(totalCount%10) {
            0L -> totalCount/page.pageSize
            else -> totalCount/page.pageSize + 1
        }

        val likeList = BoardLikeList(
            totalLikeCount = totalCount,
            currentPage = page.pageNumber.toLong() + 1,
            totalPage = totalPage,
            likeList = response
        )
        return likeList
    }

    fun resolveToken(token: String): String? {
        return if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }
}
package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.board.BoardLikeResponseDto
import com.project.jiguhada.controller.dto.board.refactor.BoardLikeItem
import com.project.jiguhada.controller.dto.board.refactor.BoardLikeList
import com.project.jiguhada.controller.dto.boardcomment.CommentLikeCount
import com.project.jiguhada.domain.board.BoardCommentLike
import com.project.jiguhada.domain.board.BoardLike
import com.project.jiguhada.exception.RequestBoardIdNotMatched
import com.project.jiguhada.exception.UserAlreadyLikeBoard
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.board.BoardCommentLikeRepository
import com.project.jiguhada.repository.board.BoardCommentRepository
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
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val boardCommentLikeRepository: BoardCommentLikeRepository,
    private val boardCommentRepository: BoardCommentRepository
) {
    @Transactional
    fun createLike(boardId: Long, userId: Long, token: String): List<BoardLikeItem> {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val boardLikeTrueList = boardLikeRepository.findByBoard_Id(boardId).filter { it.isLike == 1 }.map { it.userEntity.username }

        boardLikeRepository.findByBoard_Id(boardId).forEach {
            println(it.toString())
        }
        val boardLikeFalseList = boardLikeRepository.findByBoard_Id(boardId).filter { it.isLike == 0 }.map { it.userEntity.username }

        if(boardLikeTrueList.contains(usernameFromToken)) {
            throw UserAlreadyLikeBoard("?????? ???????????? ??????????????????.")
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

        // return boardLikeRepository.findByBoard_Id(boardId).filter { it.isLike == 1 }.map { it.toResponse() }
        return boardLikeRepository.findTop10LikeByDateDesc(boardId)
    }

    /**
     * ?????? ???????????? ????????? ????????? ????????? ???????????? ???????????????????
     * ????????? ????????? ???..
     * ????????? ????????? ???????????? ?????? ????????? ?????? ??? ?????? ??????????????? ??? ?????????..?
     * ????????? ????????? ????????? ????????? ????????? ?????????
     */
    @Transactional
    fun createBoardCommentLike(
        boardId: Long,
        userId: Long,
        commentId: Long
    ): CommentLikeCount {
        val board = boardRepository.findById(boardId).get()
        val user = userEntityRepository.findById(userId).get()
        val boardComment = boardCommentRepository.findById(commentId).get()
        // ????????? ??????
        val boardLike = boardCommentLikeRepository.findByUserEntityIdAndBoardCommentId(userId,commentId)
        if(boardLike?.whetherHeart == 0L) {
            throw UserAlreadyLikeBoard("?????? ???????????? ??????????????????.")
        } else if(boardLike?.whetherHeart == 1L) {
            // ????????? ?????? ???????????? ?????????
            val boardLike = boardCommentLikeRepository.findByUserEntityIdAndBoardCommentId(userId, commentId)
            boardLike?.createLike()
        } else {
            // ????????? ???????????? ???????????? ?????????
            val boardCommentLike = BoardCommentLike(
                whetherHeart = 0,
                board = board,
                userEntity = user,
                boardComment = boardComment
            )
            boardCommentLikeRepository.save(boardCommentLike) // ?????? ?????????
        }
        boardComment.addCommentLikeCount()
        println("????????? ${boardComment.likeCount}")
        return CommentLikeCount(
            likeCount = boardComment.likeCount
        )
    }

    /**
     * ?????? ????????? ????????? boardComment likecount ?????? -1 ?????????
     * likecount??? ???????????????
     */
    @Transactional
    fun deleteCommentLike(boardCommentId: Long, boardId: Long, username: String): CommentLikeCount {
        val user = userEntityRepository.findByUsername(username).get()
        val board = boardCommentRepository.findById(boardCommentId).get()
        val boardLikeResponse =
            boardCommentLikeRepository.findByUserEntityIdAndBoardCommentId(user.id!!, boardCommentId)
        boardLikeResponse?.deleteLike()
        val response = board.deleteCommentLikeCount()
        return CommentLikeCount(
            likeCount = response.likeCount
        )
    }


    @Transactional
    fun deleteLike(likeId: Long, token: String): List<BoardLikeResponseDto> {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val like = boardLikeRepository.findById(likeId).get()
        if(like.userEntity.username.equals(usernameFromToken)) {
            like.deleteLike()
            return boardLikeRepository.findByBoard_Id(like.board.id!!).filter { it.isLike == 0 }.map { it.toResponse() }
        } else {
            throw RequestBoardIdNotMatched("????????? ?????? ???????????????")
        }
    }

    @Transactional
    fun readBoardLikes(
        boardId: Long,
        page: Pageable
    ): BoardLikeList {
        val response = boardLikeRepository.findBoardLikeByBoardId(boardId, page)
        val totalCount = boardLikeRepository.countByBoard_Id(boardId)
        println(totalCount)
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
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

        // return boardLikeRepository.findByBoard_Id(boardId).filter { it.isLike == 1 }.map { it.toResponse() }
        return boardLikeRepository.findTop10LikeByDateDesc(boardId)
    }

    /**
     * 댓글 좋아요랑 좋아요 취소를 하나의 메서드로 만들수있을듯?
     * 그치만 지금은 ㅠ..
     * 좋아요 구현시 테이블에 계속 가지고 있는 것 보다 삭제하는게 더 나을듯..?
     * 댓글이 좋아요 개수를 가지고 있는게 좋을듯
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
        // 좋아요 목록
        val boardLike = boardCommentLikeRepository.findByUserEntityIdAndBoardCommentId(userId,commentId)
        if(boardLike?.whetherHeart == 0L) {
            throw UserAlreadyLikeBoard("이미 좋아요를 누르셨습니다.")
        } else if(boardLike?.whetherHeart == 1L) {
            // 좋아요 취소 리스트에 있다면
            val boardLike = boardCommentLikeRepository.findByUserEntityIdAndBoardCommentId(userId, commentId)
            boardLike?.createLike()
        } else {
            // 좋아요 테이블에 아무것도 없다면
            val boardCommentLike = BoardCommentLike(
                whetherHeart = 0,
                board = board,
                userEntity = user,
                boardComment = boardComment
            )
            boardCommentLikeRepository.save(boardCommentLike) // 댓글 좋아요
        }
        boardComment.addCommentLikeCount()
        println("좋아요 ${boardComment.likeCount}")
        return CommentLikeCount(
            likeCount = boardComment.likeCount
        )
    }

    /**
     * 댓글 좋아요 취소시 boardComment likecount 필드 -1 해주기
     * likecount만 리턴해주기
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
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
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
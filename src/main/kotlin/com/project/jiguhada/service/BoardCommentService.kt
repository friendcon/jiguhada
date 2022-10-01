package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.boardcomment.*
import com.project.jiguhada.domain.board.BoardComment
import com.project.jiguhada.exception.RequestBoardIdNotMatched
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.board.BoardCommentRepository
import com.project.jiguhada.repository.board.BoardRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.SecurityUtil
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

@Service
class BoardCommentService(
    private val boardRepository: BoardRepository,
    private val userEntityRepository: UserEntityRepository,
    private val boardCommentRepository: BoardCommentRepository,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {

    @Transactional
    fun readsBoardComments(
        boardId: Long,
        page: Pageable
    ): BoardCommentList {
        val response = boardCommentRepository.findCommentList(boardId, page)
        val totalCount = boardCommentRepository.countByBoard_Id(boardId)
        val totalPage = when(totalCount%5) {
            0L -> totalCount/page.pageSize
            else -> totalCount/page.pageSize + 1
        }

        val commentList = BoardCommentList(
            totalCommentCount = totalCount,
            currentPage = page.pageNumber.toLong() + 1,
            totalPage = totalPage,
            commentList = response
        )
        return commentList
    }

    @Transactional
    fun createComment(commentRequestDto: CommentRequestDto): BoardCommentList {
        val comment = commentRequestDto.toEntity(SecurityUtil.currentUsername)
        boardCommentRepository.save(comment)
        val totalCount = boardCommentRepository.countByBoard_Id(commentRequestDto.boardId)
        val totalPage = when(totalCount%5) {
            0L -> totalCount/5
            else -> totalCount/5 + 1
        }
        val comments = boardCommentRepository.findTop5ByBoardCommentNullOrderByCreatedDateDesc().map { it.toBoardCommentItem() }
        val commentList = BoardCommentList(
            totalCommentCount = totalCount,
            currentPage = 1,
            totalPage = totalPage,
            commentList = comments
        )
        return commentList
    }

    // 대댓글 작성
    @Transactional
    fun createReComment(reCommentRequestDto: ReCommentRequestDto): BoardCommentList {
        val recomment = reCommentRequestDto.toEntity(SecurityUtil.currentUsername)
        boardCommentRepository.save(recomment)
        val totalCount = boardCommentRepository.countByBoard_Id(reCommentRequestDto.boardId)
        val totalPage = when(totalCount%5) {
            0L -> totalCount/5
            else -> totalCount/5 + 1
        }
        val comments = boardCommentRepository.findTop5ByBoardCommentNullOrderByCreatedDateDesc().map { it.toBoardCommentItem() }
        return BoardCommentList(
            totalCommentCount = totalCount,
            currentPage = 1,
            totalPage = totalPage,
            commentList = comments
        )
    }

    // 업데이트 할 댓글 가져옴
    @Transactional
    fun getUpdateComment(commentId: Long, token: String): CommentUpdateResponseDto {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val comment = boardCommentRepository.findById(commentId).get()
        if(comment.userEntity.username.equals(usernameFromToken)) {
            return comment.toUpdateCommentResponse()
        } else {
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
        }
    }

    @Transactional
    fun updateComment(commentUpdateRequestDto: CommentUpdateRequestDto): BoardCommentList {
        val comment = boardCommentRepository.findById(commentUpdateRequestDto.commentId).get()
        if(comment.userEntity.username.equals(SecurityUtil.currentUsername)) {
            comment.updateComment(commentUpdateRequestDto) // 댓글 업데이트
            val totalCount = boardCommentRepository.countByBoard_Id(comment.board.id!!)
            val totalPage = when(totalCount%5) {
                0L -> totalCount/5
                else -> totalCount/5 + 1
            }
            val comments = boardCommentRepository.findTop5ByBoardCommentNullOrderByCreatedDateDesc().map { it.toBoardCommentItem() }
            return BoardCommentList(
                totalCommentCount = totalCount,
                currentPage = 1,
                totalPage = totalPage,
                commentList = comments
            )
        } else {
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
        }
    }

    @Transactional
    fun deleteComment(commentId: Long): BoardCommentList {
        val comment = boardCommentRepository.findById(commentId).get()
        val boardId = comment.board.id
        if(comment.userEntity.username.equals(SecurityUtil.currentUsername)) {
            boardCommentRepository.delete(comment) // 삭제
            val totalCount = boardCommentRepository.countByBoard_Id(boardId!!)
            val totalPage = when(totalCount%5) {
                0L -> totalCount/5
                else -> totalCount/5 + 1
            }
            val comments = boardCommentRepository.findTop5ByBoardCommentNullOrderByCreatedDateDesc().map { it.toBoardCommentItem() }
            return BoardCommentList(
                totalCommentCount = totalCount,
                currentPage = 1,
                totalPage = totalPage,
                commentList = comments
            )
        } else {
            throw RequestBoardIdNotMatched("권한이 없는 요청입니다")
        }
    }

    fun resolveToken(token: String): String? {
        return if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }

    fun CommentRequestDto.toEntity(username: String): BoardComment {
        val board = boardRepository.findById(boardId).get()
        val user = userEntityRepository.findByUsername(username).get()
        return BoardComment(
            board = board,
            userEntity = user,
            content = content
        )
    }

    fun ReCommentRequestDto.toEntity(username: String): BoardComment {
        val board = boardRepository.findById(boardId).get()
        val user = userEntityRepository.findByUsername(username).get()
        val parentComment = boardCommentRepository.findById(parentCommentId).get()
        return BoardComment(
            board = board,
            userEntity = user,
            content = content,
            boardComment = parentComment
        )
    }
}
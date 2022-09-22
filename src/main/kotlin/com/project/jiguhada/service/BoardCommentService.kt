package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.board.CommentResponseDto
import com.project.jiguhada.controller.dto.boardcomment.CommentRequestDto
import com.project.jiguhada.controller.dto.boardcomment.ReCommentRequestDto
import com.project.jiguhada.domain.board.BoardComment
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.board.BoardCommentRepository
import com.project.jiguhada.repository.board.BoardRepository
import com.project.jiguhada.repository.user.UserEntityRepository
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
    // 댓글 작성
    @Transactional
    fun createComment(commentRequestDto: CommentRequestDto, token: String): List<CommentResponseDto> {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val comment = commentRequestDto.toEntity(usernameFromToken)
        boardCommentRepository.save(comment)
        return boardCommentRepository.findByBoardCommentNullOrderByCreatedDateAsc().map { it.toResponse() }
    }

    // 대댓글 작성
    @Transactional
    fun createReComment(reCommentRequestDto: ReCommentRequestDto, token: String): List<CommentResponseDto> {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)
        val recomment = reCommentRequestDto.toEntity(usernameFromToken)
        boardCommentRepository.save(recomment)
        return boardCommentRepository.findByBoardCommentNullOrderByCreatedDateAsc().map { it.toResponse() }
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
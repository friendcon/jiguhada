package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.board.CommentResponseDto
import com.project.jiguhada.controller.dto.boardcomment.CommentRequestDto
import com.project.jiguhada.controller.dto.boardcomment.ReCommentRequestDto
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.BoardCommentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@Tag(name = "Board Comment API")
@RequestMapping("/api/v1/boardComment")
class BoardCommentController(
    private val boardCommentService: BoardCommentService,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @PostMapping("/create")
    @Operation(summary = "댓글 작성")
    fun createBoardComment(
        @RequestBody commentRequestDto: CommentRequestDto,
        httprequest: HttpServletRequest
    ): ResponseEntity<List<CommentResponseDto>> {
        val response = boardCommentService.createComment(commentRequestDto, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/createReply")
    @Operation(summary = "대댓글 작성")
    fun createBoardReComment(
        @RequestBody commentRequestDto: ReCommentRequestDto,
        httprequest: HttpServletRequest
    ): ResponseEntity<List<CommentResponseDto>> {
        val response = boardCommentService.createReComment(commentRequestDto, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
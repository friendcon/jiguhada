package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.boardcomment.*
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.BoardCommentService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import kotlin.math.absoluteValue

@RestController
@Tag(name = "게시판 댓글 API")
@RequestMapping("/api/v1/boardComment")
class BoardCommentController(
    private val boardCommentService: BoardCommentService,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @GetMapping("/read/{boardId}")
    @Operation(summary = "게시글 댓글 리스트 with 페이징")
    fun getBoardCommentLists(
        @PathVariable("boardId") boardId: Long,
        @RequestParam("page") page: Long?
    ): ResponseEntity<BoardCommentList> {
        val currentPage = when (page) {
            null, 1L -> 0
            else -> page.absoluteValue - 1
        }
        val response = boardCommentService.readsBoardComments(boardId, PageRequest.of(currentPage.toInt(), 5))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/create")
    @Operation(summary = "댓글 작성")
    fun createBoardComment(
        @RequestBody commentRequestDto: CommentRequestDto,
        httprequest: HttpServletRequest
    ): ResponseEntity<BoardCommentList> {
        val response = boardCommentService.createComment(commentRequestDto)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/createReply")
    @Operation(summary = "대댓글 작성")
    fun createBoardReComment(
        @RequestBody commentRequestDto: ReCommentRequestDto,
        httprequest: HttpServletRequest
    ): ResponseEntity<BoardCommentList> {
        val response = boardCommentService.createReComment(commentRequestDto)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/update/{id}")
    @Operation(summary = "댓글 수정을 위해 데이터 가져오는 API")
    fun getUpdateComment(
        @PathVariable("id") commentId: Long,
        httprequest: HttpServletRequest
    ): ResponseEntity<CommentUpdateResponseDto> {
        val response = boardCommentService.getUpdateComment(commentId, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PutMapping("/update")
    @Operation(summary = "댓글 수정")
    fun updateComment(
        @RequestBody commentUpdateRequestDto: CommentUpdateRequestDto,
        httprequest: HttpServletRequest
    ): ResponseEntity<BoardCommentList> {
        val response = boardCommentService.updateComment(commentUpdateRequestDto)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteComment(
        @PathVariable("id") commentId: Long,
        httprequest: HttpServletRequest
    ): ResponseEntity<BoardCommentList> {
        val response = boardCommentService.deleteComment(commentId)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
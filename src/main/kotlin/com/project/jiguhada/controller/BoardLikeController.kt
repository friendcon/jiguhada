package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.board.BoardLikeResponseDto
import com.project.jiguhada.controller.dto.board.refactor.BoardLikeItem
import com.project.jiguhada.controller.dto.board.refactor.BoardLikeList
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.BoardLikeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest
import kotlin.math.absoluteValue

@RestController
@Tag(name = "게시글 좋아요 API")
@RequestMapping("/api/v1/boardLike")
class BoardLikeController(
    private val boardLikeService: BoardLikeService,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @PostMapping("/create/{boardid}")
    @Operation(summary = "게시글 좋아요")
    fun createLike(
        @PathVariable("boardid") boardId: Long,
        @RequestParam("userId") userId: Long,
        httprequest: HttpServletRequest
    ): ResponseEntity<List<BoardLikeItem>> {
        val response = boardLikeService.createLike(boardId, userId, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @DeleteMapping("/delete/{likeId}")
    @Operation(summary = "게시글 좋아요 취소")
    fun deleteLike(
        @PathVariable("likeId") likeId: Long,
        httprequest: HttpServletRequest
    ): ResponseEntity<List<BoardLikeResponseDto>> {
        val response = boardLikeService.deleteLike(likeId, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/read/{id}")
    @Operation(summary = "게시글 좋아요 조회")
    fun readLike(
        @PathVariable("id") boardId: Long,
        @RequestParam("page") page: Long?
    ): ResponseEntity<BoardLikeList> {
        val currentPage = when (page) {
            null, 1L -> 0
            else -> page.absoluteValue - 1
        }
        val response = boardLikeService.readBoardLikes(boardId, PageRequest.of(currentPage.toInt(), 10))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
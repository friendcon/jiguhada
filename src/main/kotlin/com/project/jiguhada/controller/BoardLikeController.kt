package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.board.BoardLikeResponseDto
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.BoardLikeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@Tag(name = "Board Like API")
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
    ): ResponseEntity<List<BoardLikeResponseDto>> {
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
}
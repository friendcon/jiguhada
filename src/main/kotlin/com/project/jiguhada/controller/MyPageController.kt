package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.board.BoardListResponse
import com.project.jiguhada.controller.dto.boardcomment.BoardCommentList
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.MypageService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import kotlin.math.abs

@RestController
@Tag(name = "사용자 마이페이지 API")
@RequestMapping("/api/v1/mypage")
class MyPageController(
    private val mypageService: MypageService,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @GetMapping("/board")
    @Operation(summary = "user 작성글 조회")
    fun getBoards(
        @RequestParam(required = false, value = "page") page: Long?,
        httprequest: HttpServletRequest
    ): ResponseEntity<BoardListResponse> {
        val page = when(page) {
            0L, null -> 0
            else -> abs(page) - 1
        }
        val response = mypageService.getUserBoards(PageRequest.of(page.toInt(), 15), jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/comment")
    @Operation(summary = "user 작성댓글 조회")
    fun getComments(
        @RequestParam(required = false, value = "page") page: Long?,
        httprequest: HttpServletRequest
    ): ResponseEntity<BoardCommentList> {
        val page = when(page) {
            0L, null -> 0
            else -> abs(page) - 1
        }
        val response = mypageService.getUserComments(PageRequest.of(page.toInt(), 15), jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.board.BoardCreateRequestDto
import com.project.jiguhada.controller.dto.board.BoardResponseDto
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.BoardService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@Tag(name = "Board API")
@RequestMapping("/api/v1/board")
class BoardController(
    private val boardService: BoardService,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @PostMapping("/create")
    fun createBoard(@RequestBody boardCreateRequestDto: BoardCreateRequestDto, httprequest: HttpServletRequest): ResponseEntity<BoardResponseDto> {
        val response = boardService.createBoard(boardCreateRequestDto, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
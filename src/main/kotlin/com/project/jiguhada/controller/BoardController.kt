package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.CommonResponseDto
import com.project.jiguhada.controller.dto.board.BoardCreateRequestDto
import com.project.jiguhada.controller.dto.board.BoardListItemResponse
import com.project.jiguhada.controller.dto.board.BoardListResponse
import com.project.jiguhada.controller.dto.board.BoardResponseDto
import com.project.jiguhada.exception.LimitFileCountException
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.BoardService
import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.BOARD_ORDER_TYPE
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
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

    @PostMapping("/uploadImg", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    fun uploadBoardImg(@RequestParam("imgFiles") multipartFiles: List<MultipartFile>): List<String> {
        if(multipartFiles.size > 3) {
            throw LimitFileCountException("업로드 할 수 있는 파일 개수를 초과하였습니다.")
        }
        return boardService.uploadBoardImg(multipartFiles)
    }

    @GetMapping("/list")
    fun getBoardList(
        @RequestParam(required = false, value = "query") query: String?,
        @RequestParam(required = false, value = "page") page: Int?,
        @RequestParam(required = false, value = "order") order: BOARD_ORDER_TYPE?,
        @RequestParam(required = false, value = "category") category: BOARD_CATEGORY?
    ): ResponseEntity<BoardListResponse> {
        val pageNum = when(page) {
            0, null -> 0
            else -> Math.abs(page) - 1
        }
        return ResponseEntity(boardService.readBoardList(query, order, category, PageRequest.of(pageNum, 15)), HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    fun deleteBoard(
        @PathVariable("id") boardId: Long, httprequest: HttpServletRequest
    ): ResponseEntity<CommonResponseDto> {
        val response = boardService.removeBoard(boardId, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
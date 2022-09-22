package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.CommonResponseDto
import com.project.jiguhada.controller.dto.board.*
import com.project.jiguhada.controller.dto.user.ImgUrlResponseDto
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.BoardService
import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.BOARD_ORDER_TYPE
import com.project.jiguhada.util.BOARD_SEARCH_TYPE
import io.swagger.v3.oas.annotations.Operation
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
    @Operation(summary = "게시글 작성")
    fun createBoard(@RequestBody boardCreateRequestDto: BoardCreateRequestDto, httprequest: HttpServletRequest): ResponseEntity<BoardResponseDto> {
        val response = boardService.createBoard(boardCreateRequestDto, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/uploadImg", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "게시글 이미지 업로드")
    fun uploadBoardImg(@RequestParam("imgFile") multipartFile: MultipartFile): ResponseEntity<ImgUrlResponseDto> {
        return ResponseEntity(boardService.uploadBoardImg(multipartFile), HttpStatus.OK)
    }

    @GetMapping("/read/{id}")
    @Operation(summary = "게시글 조회")
    fun readBoard(
        @PathVariable("id") boardId: Long
    ): ResponseEntity<BoardResponseDto> {
        val response = boardService.readBoard(boardId)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/update/{id}")
    @Operation(summary = "게시글 수정을 위한 데이터 가져오는 API")
    fun getUpdateBoard(
        @PathVariable("id") boardId: Long,
        httprequest: HttpServletRequest
    ): ResponseEntity<BoardUpdateResponseDto> {
        val response = boardService.getUpdateBoard(boardId, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PutMapping("/update")
    @Operation(summary = "게시글 수정")
    fun updatdBoard(
        @RequestBody boardUpdateRequestDto: BoardUpdateRequestDto,
        httprequest: HttpServletRequest
    ): ResponseEntity<BoardResponseDto>{
        val response = boardService.updateBoard(boardUpdateRequestDto, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/list")
    @Operation(summary = "게시글 목록 조회")
    fun getBoardList(
        @RequestParam(required = false, value = "query") query: String?,
        @RequestParam(required = false, value = "page") page: Int?,
        @RequestParam(required = false, value = "order") order: BOARD_ORDER_TYPE?,
        @RequestParam(required = false, value = "category") category: BOARD_CATEGORY?,
        @RequestParam(required = false, value = "searchType") searchType: BOARD_SEARCH_TYPE?
    ): ResponseEntity<BoardListResponse> {
        val pageNum = when(page) {
            0, null -> 0
            else -> Math.abs(page) - 1
        }
        return ResponseEntity(boardService.readBoardList(query, order, category, PageRequest.of(pageNum, 15), searchType), HttpStatus.OK)
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "게시글 삭제")
    fun deleteBoard(
        @PathVariable("id") boardId: Long, httprequest: HttpServletRequest
    ): ResponseEntity<CommonResponseDto> {
        val response = boardService.removeBoard(boardId, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
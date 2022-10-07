package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.board.BoardListResponse
import com.project.jiguhada.controller.dto.boardcomment.BoardCommentList
import com.project.jiguhada.domain.user.UserEntity
import com.project.jiguhada.exception.UserInfoIsPrivateException
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.service.MypageService
import com.project.jiguhada.util.IS_USER_INFO_PUBLIC
import com.project.jiguhada.util.SecurityUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest
import kotlin.math.abs

@RestController
@Tag(name = "사용자 페이지 확인 API")
@RequestMapping("/api/v1/userinfo")
class MyPageController(
    private val mypageService: MypageService,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val userEntityRepository: UserEntityRepository
) {
    @GetMapping("/board/{username}")
    @Operation(summary = "사용자 작성글 조회")
    fun getBoards(
        @PathVariable("username") username: String,
        @RequestParam(required = false, value = "page") page: Long?,
        httprequest: HttpServletRequest
    ): ResponseEntity<BoardListResponse> {
        val page = when(page) {
            0L, null -> 0
            else -> abs(page) - 1
        }

        val response = mypageService.getUserBoards(PageRequest.of(page.toInt(), 15), jwtAuthenticationProvider.getTokenFromHeader(httprequest))

        if(SecurityUtil.currentUsername == username) {
            return ResponseEntity(response, HttpStatus.OK)
        } else if(getUserEntity(username).isUserInfoPublic == IS_USER_INFO_PUBLIC.PRIVATE) {
            throw UserInfoIsPrivateException("회원 정보가 비공개 상태입니다.")
        }

        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/comment/{username}")
    @Operation(summary = "사용자 작성댓글 조회")
    fun getComments(
        @PathVariable("username") username: String,
        @RequestParam(required = false, value = "page") page: Long?,
        httprequest: HttpServletRequest
    ): ResponseEntity<BoardCommentList> {
        val page = when(page) {
            0L, null -> 0
            else -> abs(page) - 1
        }
        val response = mypageService.getUserComments(PageRequest.of(page.toInt(), 15), jwtAuthenticationProvider.getTokenFromHeader(httprequest))

        if(SecurityUtil.currentUsername == username) {
            return ResponseEntity(response, HttpStatus.OK)
        } else if(getUserEntity(username).isUserInfoPublic == IS_USER_INFO_PUBLIC.PRIVATE) {
            throw UserInfoIsPrivateException("회원 정보가 비공개 상태입니다.")
        }
        return ResponseEntity(response, HttpStatus.OK)
    }

    fun getUserEntity(username: String): UserEntity {
        return userEntityRepository.findByUsername(username).get()
    }
}
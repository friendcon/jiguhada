package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.CommonResponseDto
import com.project.jiguhada.controller.dto.TokenDto
import com.project.jiguhada.controller.dto.user.*
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.service.AwsS3Service
import com.project.jiguhada.service.MypageService
import com.project.jiguhada.service.UserService
import com.project.jiguhada.util.IS_USER_INFO_PUBLIC
import com.project.jiguhada.util.SecurityUtil
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import javax.servlet.http.HttpServletRequest
import javax.validation.Valid


@RestController
@Tag(name = "사용자 API")
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
    private val userEntityRepository: UserEntityRepository,
    private val mypageService: MypageService,
    private val awsS3Service: AwsS3Service,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @GetMapping("/checkDuplicate")
    @Operation(summary = "사용자 ID 중복체크")
    fun checkDuplicateUsername(@RequestParam("username") username: String): Boolean {
        return userService.checkUsernameDuplicate(username)
    }

    @GetMapping("/checkDuplicateNickname")
    @Operation(summary = "사용자 닉네임 중복체크")
    fun checkDuplicateNickname(@RequestParam("nickname") nickname: String): Boolean {
        return userService.checkNicknameDuplicate(nickname)
    }

    @PostMapping("/uploadTempImg", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "회원가입 이미지 첨부")
    fun uploadTempImg(@RequestParam("imgFile") multipartFile: MultipartFile): ResponseEntity<ImgUrlResponseDto> {
        return ResponseEntity.ok(ImgUrlResponseDto(awsS3Service.uploadImgToDir(multipartFile, "temp")))
    }
    @PostMapping("/signup")
    @Operation(summary = "회원가입")
    fun signUp(@RequestBody @Valid reqeust: CreateUserRequestDto): ResponseEntity<TokenDto> {
        return ResponseEntity.ok(userService.signUp(reqeust))
    }

    @GetMapping("/info/{username}")
    @Operation(summary = "회원정보조회")
    fun readUserInfo(@PathVariable("username") username: String, @RequestHeader("Authorization") accessToken: String): ResponseEntity<ReadUserInfoResponseDto> {
        val response = userService.readUserInfo(accessToken, username) ?: return ResponseEntity(HttpStatus.BAD_REQUEST)
        return ResponseEntity.ok().body(response)
    }

    @PutMapping("/updateNickname")
    @Operation(summary = "닉네임 수정")
    fun updateNickname(@RequestBody request: UserNicknameRequestDto, httprequest: HttpServletRequest): ResponseEntity<CommonResponseDto> {
        val response = userService.updateUserNickname(request.nickname, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity.ok().body(response)
    }

    @PutMapping("/updateUserInfoPublic")
    fun updateUserInfoIsPublic(
        @RequestParam("isPublic") isUserInfoPublic: IS_USER_INFO_PUBLIC
    ): ResponseEntity<CommonResponseDto> {
        val user = userEntityRepository.findByUsername(SecurityUtil.currentUsername).get()

        val response = mypageService.setUserInfoPublic(user.id!!, isUserInfoPublic)
        return ResponseEntity(response, HttpStatus.OK)
    }
    @PostMapping("/updatePassword")
    @Operation(summary = "비밀번호 변경")
    fun updatePassword(@RequestBody request: UserPasswordRequestDto, httprequest: HttpServletRequest): ResponseEntity<CommonResponseDto> {
        val response = userService.updateUserPassword(request, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity.ok().body(response)
    }

    @PostMapping("/updateImg", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "이미지 수정")
    fun updateImgUrl(@RequestParam("imgFile") imgFile: MultipartFile, httprequest: HttpServletRequest): ResponseEntity<ImgUrlResponseDto> {
        jwtAuthenticationProvider.getTokenFromHeader(httprequest)
        val response = userService.updateUserImgUrl(imgFile, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity.ok().body(response)
    }

    @DeleteMapping("/signOut")
    @Operation(summary = "회원탈퇴")
    fun signOutUser(
        @RequestBody passwordRequestDto: PasswordRequestDto,
        httprequest: HttpServletRequest
    ): ResponseEntity<CommonResponseDto> {
        val response = userService.singOutUser(passwordRequestDto, jwtAuthenticationProvider.getTokenFromHeader(httprequest))
        return ResponseEntity(response, HttpStatus.OK)
    }
}
package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.ImgUrlResponseDto
import com.project.jiguhada.service.AwsS3Service
import com.project.jiguhada.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "User API")
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService,
    private val awsS3Service: AwsS3Service
) {
    @GetMapping("/checkDuplicate")
    @Operation(summary = "사용자 ID 중복체크")
    fun checkDuplicate(@RequestParam("username") username: String): Boolean {
        return userService.checkUsernameDuplicate(username)
    }

    @PostMapping("/uploadTempImg")
    @Operation(summary = "회원가입 이미지 첨부")
    fun uploadTempImg(@RequestParam("imgFile") multipartFile: MultipartFile): ResponseEntity<ImgUrlResponseDto> {
        return ResponseEntity.ok(ImgUrlResponseDto(awsS3Service.uploadImgToTemp(multipartFile)))
    }
}
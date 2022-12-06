package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.CommonResponseDto
import com.project.jiguhada.controller.dto.challenge.ChallengeAuthItem
import com.project.jiguhada.controller.dto.challenge.ChallengeAuthListResponse
import com.project.jiguhada.controller.dto.challenge.ChallengeAuthRequest
import com.project.jiguhada.controller.dto.user.ImgUrlResponseDto
import com.project.jiguhada.service.ChallengeAuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile
import kotlin.math.absoluteValue

@RestController
@Tag(name = "챌린지 인증 API")
@RequestMapping("/api/v1/authchallenge")
class ChallengeAuthController(
    private val challengeAuthService: ChallengeAuthService
) {

    @GetMapping("/list/{challengeId}")
    @Operation(summary = "챌린지 인증 댓글 리스트 조회")
    fun readChallengeAuthList(@PathVariable("challengeId") challengeId: Long, page: Long?): ResponseEntity<ChallengeAuthListResponse> {
        val pageable = when(page) {
            0L, 1L, null -> PageRequest.of(0, 10)
            else -> PageRequest.of(page.absoluteValue.toInt(), 10)
        }
        val response = challengeAuthService.readChallengeAuthList(challengeId, pageable)
        return ResponseEntity(response, HttpStatus.OK)
    }
    @PostMapping("/create")
    @Operation(summary = "챌린지 인증 댓글 생성")
    fun createChallengeAuth(@RequestBody challengeAuthRequest: ChallengeAuthRequest): ResponseEntity<ChallengeAuthItem>{
        val response = challengeAuthService.createChallengeAuth(challengeAuthRequest)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/uploadAuthImg", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "챌린지 인증 이미지 첨부")
    fun createChallengeAuthImg(
        @RequestParam("imgFile") multipartFile: MultipartFile
    ): ResponseEntity<ImgUrlResponseDto> {
        val response = challengeAuthService.createChallengeAuthImg(multipartFile)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PutMapping("/approve/{challengeAuthId}")
    @Operation(summary = "챌린지 인증 승인")
    fun approveChallengeAuth(
        @PathVariable("challengeAuthId") challengeAuthId: Long
    ): ResponseEntity<CommonResponseDto> {
        val response = challengeAuthService.approveChallengeAuth(challengeAuthId)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PutMapping("/refuse/{challengeAuthId}")
    @Operation(summary = "챌린지 인증 거절")
    fun refuseChallengeAuth(
        @PathVariable("challengeAuthId") challengeAuthId: Long
    ): ResponseEntity<CommonResponseDto> {
        val response = challengeAuthService.refuseChallengeAuth(challengeAuthId)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
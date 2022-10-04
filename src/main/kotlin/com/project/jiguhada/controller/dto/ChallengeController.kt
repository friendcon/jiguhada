package com.project.jiguhada.controller.dto

import com.project.jiguhada.controller.dto.challenge.ChallengeCreateRequest
import com.project.jiguhada.controller.dto.challenge.ChallengeCreateResponse
import com.project.jiguhada.controller.dto.challenge.ChallengeJoinRequest
import com.project.jiguhada.controller.dto.user.ImgUrlResponseDto
import com.project.jiguhada.service.ChallengeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "챌린지 API")
@RequestMapping("/api/v1/challenge")
class ChallengeController(
    private val challengeService: ChallengeService
) {
    @PostMapping("/create")
    @Operation(summary = "챌린지 생성")
    fun createChallenge(@RequestBody challengeCreateRequest: ChallengeCreateRequest): ResponseEntity<ChallengeCreateResponse> {
        val response = challengeService.createChallenge(challengeCreateRequest)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/img/upload", consumes = [MediaType.MULTIPART_FORM_DATA_VALUE])
    @Operation(summary = "챌린지 대표 및 추가 이미지")
    fun uploadChallengeProfileImg(@RequestParam("imgFile") multipartFile: MultipartFile): ResponseEntity<ImgUrlResponseDto> {
        val response = challengeService.uploadChallengeImg(multipartFile)
        return ResponseEntity(response,HttpStatus.OK)
    }

    @PostMapping("/join")
    fun joinChallenge(@RequestBody challengeJoinRequest: ChallengeJoinRequest): ResponseEntity<ChallengeCreateResponse> {
        val response = challengeService.joinChallenge(challengeJoinRequest)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
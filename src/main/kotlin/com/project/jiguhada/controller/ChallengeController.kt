package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.challenge.*
import com.project.jiguhada.controller.dto.user.ImgUrlResponseDto
import com.project.jiguhada.service.ChallengeService
import com.project.jiguhada.util.*
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

    @GetMapping("/{challengeId}")
    @Operation(summary = "챌린지 정보 조회")
    fun readChallengeInfo(@PathVariable("challengeId") id: Long): ResponseEntity<ChallengeCreateResponse> {
        val response = challengeService.readChallenge(id)
        return ResponseEntity(response, HttpStatus.OK)
    }

    @GetMapping("/list")
    @Operation(summary = "챌린지 리스트 조회")
    fun readChallengeList(
        @RequestParam("page") page: Long?,
        @RequestParam("query") query: String?,
        @RequestParam("searchType") searchType: CHALLENGE_SEARCH_TYPE?,
        @RequestParam("orderType") orderType: CHALLENGE_ORDER_TYPE?,
        @RequestParam("category") category: CHALLENGE_CATEGORY?,
        @RequestParam("status") status: CHALLENGE_STATUS?,
        @RequestParam("tagList") tagList: List<CHALLENGE_TAG>?
    ): ResponseEntity<ChallengeListResponse> {
        val page = when(page) {
            null, 0L, 1L -> 0
            else -> page.absoluteValue - 1
        }
        val response = challengeService.readChallengeList(query, searchType, orderType, category, status, tagList, PageRequest.of(page.toInt(), 20))
        return ResponseEntity(response, HttpStatus.OK)
    }

    @PostMapping("/join")
    fun joinChallenge(@RequestBody challengeJoinRequest: ChallengeJoinRequest): ResponseEntity<ChallengeCreateResponse> {
        val response = challengeService.joinChallenge(challengeJoinRequest)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
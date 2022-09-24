package com.project.jiguhada.controller.dto

import com.project.jiguhada.controller.dto.challenge.ChallengeCreateRequest
import com.project.jiguhada.controller.dto.challenge.ChallengeCreateResponse
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.ChallengeService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "챌린지 API")
@RequestMapping("/api/v1/challenge")
class ChallengeController(
    private val challengeService: ChallengeService,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    @PostMapping("/create")
    @Operation(summary = "챌린지 생성")
    fun createChallenge(@RequestBody challengeCreateRequest: ChallengeCreateRequest): ResponseEntity<ChallengeCreateResponse> {
        println(challengeCreateRequest.toString())
        val response = challengeService.createChallenge(challengeCreateRequest)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
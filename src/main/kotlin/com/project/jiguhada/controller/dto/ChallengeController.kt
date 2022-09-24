package com.project.jiguhada.controller.dto

import com.project.jiguhada.controller.dto.challenge.ChallengeCreateRequest
import com.project.jiguhada.controller.dto.challenge.ChallengeCreateResponse
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.ChallengeService
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "챌린지 API")
@RequestMapping("/api/v1/challenge")
class ChallengeController(
    private val challengeService: ChallengeService,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
) {
    fun createChallenge(challengeCreateRequest: ChallengeCreateRequest): ResponseEntity<ChallengeCreateResponse> {
        val response = challengeService.createChallenge(challengeCreateRequest)
        return ResponseEntity(response, HttpStatus.OK)
    }
}
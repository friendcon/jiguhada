package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.user.LoginRequestDto
import com.project.jiguhada.controller.dto.TokenDto
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "auth Controller")
@RequestMapping("/api/v1/auth")
class AuthController(
    private val authService: AuthService
) {
    @PostMapping("/login")
    @Operation(summary = "로그인")
    fun authorize(@RequestBody loginRequest: LoginRequestDto): ResponseEntity<TokenDto> {
        var tokenDto: TokenDto? = authService.login(loginRequest)
        if(tokenDto == null) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(tokenDto)
        }
        val httpHeaders = HttpHeaders()
        httpHeaders.add(JwtAuthenticationProvider.AUTHORIZATION_HEADER, "Bearer ${tokenDto.accessToken}")
        return ResponseEntity<TokenDto>(tokenDto, httpHeaders, HttpStatus.OK)
    }
}
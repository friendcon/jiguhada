package com.project.jiguhada.controller

import com.project.jiguhada.controller.dto.LoginRequestDto
import com.project.jiguhada.controller.dto.TokenDto
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder
) {
    @PostMapping("/login")
    fun authorize(@RequestBody loginRequest: LoginRequestDto): ResponseEntity<TokenDto> {
        println("id : ${loginRequest.username}")
        println("pw : ${loginRequest.password}")

        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)

        val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication

        val tokenDto = jwtAuthenticationProvider.createToken(authentication)
        val httpHeaders = HttpHeaders()
        httpHeaders.add(JwtAuthenticationProvider.AUTHORIZATION_HEADER, "Bearer ${tokenDto.token}")
        return ResponseEntity<TokenDto>(tokenDto, httpHeaders, HttpStatus.OK)
    }
}
package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.LoginRequestDto
import com.project.jiguhada.controller.dto.TokenDto
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder
) {
    fun login(loginRequest: LoginRequestDto): TokenDto {
        println("id : ${loginRequest.username}")
        println("pw : ${loginRequest.password}")

        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)

        val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)
        SecurityContextHolder.getContext().authentication

        return jwtAuthenticationProvider.createToken(authentication)
    }
}
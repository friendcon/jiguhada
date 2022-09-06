package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.LoginRequestDto
import com.project.jiguhada.controller.dto.TokenDto
import com.project.jiguhada.exception.UsernameNotFoundException
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.UserEntityRepository
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val authenticationManagerBuilder: AuthenticationManagerBuilder,
    private val userEntityRepository: UserEntityRepository,
    private val passwordEncoder: PasswordEncoder
) {
    fun login(loginRequest: LoginRequestDto): TokenDto? {
        val authenticationToken = UsernamePasswordAuthenticationToken(loginRequest.username, loginRequest.password)
        val isExistUsername = userEntityRepository.existsByUsername(loginRequest.username!!)

        if(!isExistUsername) {
            throw UsernameNotFoundException("존재하지 않는 아이디입니다")
        }

        val authentication: Authentication = authenticationManagerBuilder.`object`.authenticate(authenticationToken)

        SecurityContextHolder.getContext().authentication

        return jwtAuthenticationProvider.createToken(authentication)
    }
}
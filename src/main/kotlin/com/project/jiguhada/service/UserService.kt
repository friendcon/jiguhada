package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.CreateUserRequestDto
import com.project.jiguhada.controller.dto.CreateUserResponseDto
import com.project.jiguhada.controller.dto.LoginRequestDto
import com.project.jiguhada.controller.dto.TokenDto
import com.project.jiguhada.domain.Role
import com.project.jiguhada.domain.UserEntity
import com.project.jiguhada.repository.UserEntityRepository
import com.project.jiguhada.util.ROLE
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
class UserService(
    private val userEntityRepository: UserEntityRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authService: AuthService
) {
    @Transactional
    fun signUp(request: CreateUserRequestDto): TokenDto {
        if(checkUsernameDuplicate(request.username)) {
            throw Exception("중복된 아이디입니다")
        }
        userEntityRepository.save(request.toEntity())
        val tokenDto = authService.login(LoginRequestDto(request.username, request.password))
        println("token valid : ${tokenDto.accessTokenExpiredDate}")
        return tokenDto
    }
    fun checkUsernameDuplicate(username: String): Boolean {
        return userEntityRepository.existsByUsername(username)
    }

    fun checkNicknameDuplicate(nickname: String): Boolean {
        return userEntityRepository.existsByNickname(nickname)
    }

    fun CreateUserRequestDto.toEntity(): UserEntity {
        return UserEntity(
            username,
            nickname,
            passwordEncoder.encode(password),
            userImageUrl,
            socialType,
            roles = mutableSetOf(Role(ROLE.ROLE_USER))
        )
    }

    fun UserEntity.toCreateUserResponse(isSuccess: String): CreateUserResponseDto {
        return CreateUserResponseDto(
            username, nickname, isSuccess
        )
    }
}

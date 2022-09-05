package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.*
import com.project.jiguhada.domain.Role
import com.project.jiguhada.domain.UserEntity
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.UserEntityRepository
import com.project.jiguhada.util.ROLE
import com.project.jiguhada.util.SecurityUtil
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.lang.Exception

@Service
class UserService(
    private val userEntityRepository: UserEntityRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authService: AuthService,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
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

    @Transactional
    fun updateNickname(request: UserNicknameRequestDto): CommonResponseDto {
        val idFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(request.accesstoken)
        // security Context 의 username 과 token에 있는 userId가 일치하면
        if(SecurityUtil.currentUsername.equals(idFromToken)) {
            if(userEntityRepository.existsByNickname(request.nickname)) {
                return CommonResponseDto(401, "이미 존재하는 닉네임입니다.")
            }
            val response = userEntityRepository.findById(request.userid).get()
            response.updateNickname(request.nickname)
            return CommonResponseDto(
                200,
                "닉네임 변경에 성공했습니다"
            )
        }
        throw UsernameNotFoundException("해당 사용자가 존재하지 않습니다")
    }

    @Transactional
    fun updatePassword(request: UserPasswordRequestDto): CommonResponseDto {
        val idFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(request.accesstoken)

        if(SecurityUtil.currentUsername.equals(idFromToken)) {
            val response = userEntityRepository.findById(request.userid).get()
            if(passwordEncoder.matches(request.nowpassword, response.password)) {
                response.updatePassword(request.newpassword)
                return CommonResponseDto(
                    200,
                    "패스워드 변경에 성공했습니다"
                )
            } else {
                return CommonResponseDto(
                    400,
                    "패스워드가 일치하지 않습니다"
                )
            }
        }
        throw UsernameNotFoundException("해당 사용자가 존재하지 않습니다")
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

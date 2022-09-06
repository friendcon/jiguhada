package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.*
import com.project.jiguhada.domain.Role
import com.project.jiguhada.domain.UserEntity
import com.project.jiguhada.exception.UnauthorizedRequestException
import com.project.jiguhada.exception.UserIdDuplicateException
import com.project.jiguhada.exception.UserNicknameDuplicateException
import com.project.jiguhada.exception.UserNowPasswordNotMatchException
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.repository.UserEntityRepository
import com.project.jiguhada.util.ROLE
import com.project.jiguhada.util.SecurityUtil
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils

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
            throw UserIdDuplicateException("중복된 아이디입니다")
        }
        userEntityRepository.save(request.toEntity())
        val tokenDto = authService.login(LoginRequestDto(request.username, request.password))
        return tokenDto!!
    }

    @Transactional
    fun readUserInfo(accesstoken: String, userid: Long): ReadUserInfoResponseDto? {

        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(accesstoken)!!)

        if(SecurityUtil.currentUsername.equals(usernameFromToken)) {
            val userresponse = userEntityRepository.findByUsername(usernameFromToken).get()
            if(userid != userresponse.id) {
                throw UnauthorizedRequestException("권한이 없는 요청입니다.")
            }
            return userEntityRepository.findByUsername(usernameFromToken).get().toReadUserInfoResponse()
        }
        return null
    }

    @Transactional
    fun updateNickname(nickname: String, token: String): CommonResponseDto {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)

        if(SecurityUtil.currentUsername.equals(usernameFromToken)) {
            if(userEntityRepository.existsByNickname(nickname)) {
                throw UserNicknameDuplicateException("중복된 닉네임입니다")
            }
            val response = userEntityRepository.findByUsername(usernameFromToken).get()
            response.updateNickname(nickname)
            return CommonResponseDto(
                200,
                "닉네임 변경에 성공했습니다"
            )
        }
        throw UsernameNotFoundException("해당 사용자가 존재하지 않습니다")
    }

    @Transactional
    fun updatePassword(request: UserPasswordRequestDto, accesstoken: String): CommonResponseDto {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(accesstoken)!!)

        if(SecurityUtil.currentUsername.equals(usernameFromToken)) {
            val response = userEntityRepository.findByUsername(usernameFromToken).get()
            if(!passwordEncoder.matches(request.nowpassword, response.password)) {
                throw UserNowPasswordNotMatchException("현재 패스워드가 일치하지 않습니다.")
            }
            if(passwordEncoder.matches(request.nowpassword, response.password)) {
                response.updatePassword(passwordEncoder.encode(request.newpassword))
                return CommonResponseDto(
                    200,
                    "패스워드 변경에 성공했습니다"
                )
            }
        }
        throw UsernameNotFoundException("해당 사용자가 존재하지 않습니다")
    }

    fun resolveToken(token: String): String? {
        return if(StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            token.substring(7)
        } else null
    }
    fun checkUsernameDuplicate(username: String): Boolean {
        val isDuplicated = userEntityRepository.existsByUsername(username)
        if(isDuplicated){
            throw UserIdDuplicateException("중복된 아이디입니다.")
        }
        return isDuplicated
    }

    fun checkNicknameDuplicate(nickname: String): Boolean {
        val isDuplicated = userEntityRepository.existsByUsername(nickname)
        if(isDuplicated){
            throw UserNicknameDuplicateException("중복된 닉네임입니다.")
        }
        return isDuplicated
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

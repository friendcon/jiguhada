package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.CommonResponseDto
import com.project.jiguhada.controller.dto.TokenDto
import com.project.jiguhada.controller.dto.user.*
import com.project.jiguhada.domain.user.Role
import com.project.jiguhada.domain.user.UserEntity
import com.project.jiguhada.exception.*
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import com.project.jiguhada.jwt.JwtUserDetails
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.IS_USER_INFO_PUBLIC
import com.project.jiguhada.util.ROLE
import com.project.jiguhada.util.SecurityUtil
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.StringUtils
import org.springframework.web.multipart.MultipartFile

@Service
class UserService(
    private val userEntityRepository: UserEntityRepository,
    private val passwordEncoder: PasswordEncoder,
    private val authService: AuthService,
    private val awsS3Service: AwsS3Service,
    private val jwtAuthenticationProvider: JwtAuthenticationProvider,
    private val jwtUserDetailsService: JwtUserDetailsService
) {
    companion object {
        const val PROFILE_DIR_NAME = "profile-img"
    }
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
    fun readUserInfo(accesstoken: String, username: String): ReadUserInfoResponseDto? {
        val response = userEntityRepository.findByUsername(username).get().toReadUserInfoResponse()

        if(SecurityUtil.currentUsername == username) {
            return response
        } else if(response.userInfoPublic.toString() == "PRIVATE") {
            throw UserInfoIsPrivateException("회원 정보가 비공개 상태입니다.")
        }
        return response
    }

    @Transactional
    fun updateUserImgUrl(multipartFile: MultipartFile, token: String): ImgUrlResponseDto? {
        val usernameFromToken = jwtAuthenticationProvider.getIdFromTokenClaims(resolveToken(token)!!)

        if(SecurityUtil.currentUsername.equals(usernameFromToken)) {
            val url = awsS3Service.uploadImgToDir(multipartFile, PROFILE_DIR_NAME)
            val userResponse = userEntityRepository.findByUsername(usernameFromToken).get()
            userResponse.updateUserImageUrl(url)
            return ImgUrlResponseDto(url)
        }
        return throw FailToUploadImgException("이미지 수정에 실패했습니다.")
    }

    @Transactional
    fun updateUserNickname(nickname: String, token: String): CommonResponseDto {
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
    fun updateUserPassword(request: UserPasswordRequestDto, accesstoken: String): CommonResponseDto {
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

    fun singOutUser(requestDto: PasswordRequestDto, token: String): CommonResponseDto {
        val username = SecurityUtil.currentUsername
        val user = userEntityRepository.findByUsername(username).get()
        if(passwordEncoder.matches(requestDto.password, user.password)) {
            val userDetails = jwtUserDetailsService.loadUserByUsername(username) as JwtUserDetails
            userDetails.changeDisabled()
            return CommonResponseDto(200, "회원탈퇴가 완료되었습니다.")
        } else {
            throw UserNowPasswordNotMatchException("비밀번호가 일치하지 않습니다.")
        }
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
            IS_USER_INFO_PUBLIC.PRIVATE,
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

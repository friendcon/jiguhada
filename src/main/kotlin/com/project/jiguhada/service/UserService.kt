package com.project.jiguhada.service

import com.project.jiguhada.controller.dto.CreateUserRequestDto
import com.project.jiguhada.controller.dto.CreateUserResponseDto
import com.project.jiguhada.domain.UserEntity
import com.project.jiguhada.repository.UserEntityRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class UserService(
    private val userEntityRepository: UserEntityRepository
) {
    @Transactional
    fun signUp(request: CreateUserRequestDto): CreateUserResponseDto {
        val response = userEntityRepository.save(request.toEntity())
        return response.toCreateUserResponse("success")
    }

    fun checkUsernameDuplicate(username: String): Boolean {
        return userEntityRepository.existsByUsername(username)
    }

    fun CreateUserRequestDto.toEntity(): UserEntity {
        return UserEntity(
            username,
            nickname,
            password,
            userImageUrl,
            socialType
        )
    }

    fun UserEntity.toCreateUserResponse(isSuccess: String): CreateUserResponseDto {
        return CreateUserResponseDto(
            username, nickname, isSuccess
        )
    }
}

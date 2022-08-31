package com.project.jiguhada.service.user

import com.project.jiguhada.controller.dto.CreateUserRequestDto
import com.project.jiguhada.domain.user.UserEntity
import com.project.jiguhada.repository.user.UserEntityRepository
import org.springframework.stereotype.Service

@Service
class UserService(
    private val userEntityRepository: UserEntityRepository
) {

    fun signUp(request: CreateUserRequestDto): UserEntity {
        return userEntityRepository.save(request.toEntity())
    }

    fun checkUsernameDuplicate(username: String): Boolean {
        return userEntityRepository.existsByUsername(username)
    }

    fun CreateUserRequestDto.toEntity(): UserEntity {
        return UserEntity(
            username,
            password,
            userImageUrl,
            socialType
        )
    }
}
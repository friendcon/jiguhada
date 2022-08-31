package com.project.jiguhada.repository.user

import com.project.jiguhada.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserEntityRepository: JpaRepository<UserEntity, Long> {
    fun existsByUsername(username: String): Boolean
}
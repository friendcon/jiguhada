package com.project.jiguhada.repository

import com.project.jiguhada.domain.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserEntityRepository: JpaRepository<UserEntity, Long> {
    fun existsByUsername(username: String): Boolean

    fun findByUsername(username: String): UserEntity
}
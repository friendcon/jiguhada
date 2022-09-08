package com.project.jiguhada.repository.user

import com.project.jiguhada.domain.user.UserEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.Optional

interface UserEntityRepository: JpaRepository<UserEntity, Long> {
    fun existsByUsername(username: String): Boolean
    fun existsByNickname(nickname: String): Boolean
    fun findByUsername(username: String?): Optional<UserEntity>
}
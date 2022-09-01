package com.project.jiguhada.repository

import com.project.jiguhada.domain.UserEntity
import com.project.jiguhada.util.SocialType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("dev")
class UserEntityRepositoryTests(
    @Autowired private val userEntityRepository: UserEntityRepository
) {
    @BeforeEach
    fun setUp() {
        val userEntity = UserEntity(
            username = "hello11",
            nickname = "dddddd",
            password = "hello11",
            userImageUrl = "imgUrl",
            socialType = SocialType.GENERAL
        )

        userEntityRepository.save(userEntity)
    }

    @Test
    @DisplayName("아이디 중복 확인 : 성공 케이스")
    fun existsByUsernameSuccessTest() {
        val response = userEntityRepository.existsByUsername("hello")
        Assertions.assertThat(response).isTrue
    }

    @Test
    @DisplayName("아이디 중복 확인 : 실패 케이스")
    fun existsByUsernameFailTest() {
        val response = userEntityRepository.existsByUsername("wow")
        Assertions.assertThat(response).isFalse
    }
}
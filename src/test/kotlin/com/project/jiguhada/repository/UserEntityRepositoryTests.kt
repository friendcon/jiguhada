package com.project.jiguhada.repository

import com.project.jiguhada.domain.Role
import com.project.jiguhada.domain.UserEntity
import com.project.jiguhada.util.ROLE
import com.project.jiguhada.util.SocialType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.transaction.annotation.Transactional

@SpringBootTest
@ActiveProfiles("dev")
class UserEntityRepositoryTests(
    @Autowired private val userEntityRepository: UserEntityRepository,
    @Autowired private val roleRepository: RoleRepository
) {
    @BeforeEach
    fun setUp() {
        val user_role = Role(roleName = ROLE.ROLE_USER)

        val admin_role = Role(roleName = ROLE.ROLE_ADMIN)

        roleRepository.saveAll(listOf(user_role, admin_role))

        val userEntity = UserEntity(
            username = "hello",
            nickname = "dddddd",
            password = "hello11",
            userImageUrl = "imgUrl",
            socialType = SocialType.GENERAL,
            roles = mutableSetOf(Role(ROLE.ROLE_USER))
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

    @Test
    @DisplayName("회원가입")
    fun UserEntityRepositoryTests() {
        val user = UserEntity(
            "hozumi",
            "hozumi",
            "hozumi",
            "hozumi",
            SocialType.GENERAL,
            roles = mutableSetOf(Role(ROLE.ROLE_USER))
        )

        userEntityRepository.save(user)

        Assertions.assertThat(user.roles).contains(Role(ROLE.ROLE_USER))
    }

    @Test
    @Transactional
    @DisplayName("회원 정보 조회")
    fun readAllUser() {
        val response = userEntityRepository.findAll()[0]
        Assertions.assertThat(response.username).isEqualTo("hello")
    }
}
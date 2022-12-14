package com.project.jiguhada.repository

import com.project.jiguhada.domain.user.Role
import com.project.jiguhada.domain.user.UserEntity
import com.project.jiguhada.repository.challenge.ChallengeRepository
import com.project.jiguhada.repository.user.RoleRepository
import com.project.jiguhada.repository.user.UserEntityRepository
import com.project.jiguhada.util.IS_USER_INFO_PUBLIC
import com.project.jiguhada.util.ROLE
import com.project.jiguhada.util.SocialType
import org.assertj.core.api.Assertions
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.data.domain.PageRequest
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("dev")
class UserEntityRepositoryTests(
    @Autowired private val challengeRepository: ChallengeRepository,
    @Autowired private val userEntityRepository: UserEntityRepository,
    @Autowired private val roleRepository: RoleRepository,
    @Autowired private val passwordEncoder: PasswordEncoder
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
            isUserInfoPublic = IS_USER_INFO_PUBLIC.PRIVATE,
            roles = mutableSetOf(Role(ROLE.ROLE_USER)),
            isenabled = true
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
    @DisplayName("")
    fun test() {
        val response = challengeRepository.findChallengeMainList(null, PageRequest.of(0, 4));
        println(response)
    }

    @Test
    @DisplayName("회원가입")
    fun userEntityRepositoryTests() {
        val user = UserEntity(
            "hozumi",
            "hozumi",
            "hozumi",
            IS_USER_INFO_PUBLIC.PRIVATE,
            "hozumi",
            SocialType.GENERAL,
            roles = mutableSetOf(Role(ROLE.ROLE_USER)),
            isenabled = true
        )

        userEntityRepository.save(user)

        println(user.toString())
        Assertions.assertThat(user.roles).contains(Role(ROLE.ROLE_USER))
    }

    @Test
    @DisplayName("패스워드 비교")
    fun passwordTest() {
        val result = passwordEncoder.encode("project0903")
        println(result)
    }
}
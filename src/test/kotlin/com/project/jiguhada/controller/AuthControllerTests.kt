package com.project.jiguhada.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.project.jiguhada.controller.dto.user.CreateUserRequestDto
import com.project.jiguhada.controller.dto.user.LoginRequestDto
import com.project.jiguhada.domain.Role
import com.project.jiguhada.repository.RoleRepository
import com.project.jiguhada.service.UserService
import com.project.jiguhada.util.ROLE
import com.project.jiguhada.util.SocialType
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status


@SpringBootTest
@ActiveProfiles("dev")
@AutoConfigureMockMvc
class AuthControllerTests(
    @Autowired private val mockMvc: MockMvc,
    @Autowired private val userService: UserService,
    @Autowired private val roleRepository: RoleRepository
) {
    @BeforeEach
    fun setUp() {
        val role = Role(ROLE.ROLE_USER)
        roleRepository.save(role)

        val request = CreateUserRequestDto(
            "project0903",
            "project0903",
            "project0903",
            "project0903",
            SocialType.GENERAL
        )
        userService.signUp(request)
    }

    @Test
    @DisplayName("로그인 테스트")
    fun idPassword() {
        val userid = "project0903"
        val password = "project0903"
        val requestDto = LoginRequestDto(
            userid,
            password
        )

        val map = hashMapOf(Pair("username", "project0903"), Pair("password", "project0903"))

        val objectMapper = ObjectMapper()

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/auth/login")
            .content(objectMapper.writeValueAsString(map))
            .contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk)
    }
}
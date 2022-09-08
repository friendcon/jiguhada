package com.project.jiguhada.controller.dto.user

import com.project.jiguhada.util.SocialType
import javax.validation.constraints.NotBlank

data class CreateUserRequestDto(
    @field:NotBlank
    val username: String,
    @field:NotBlank
    val nickname: String,
    @field:NotBlank
    val password: String,
    @field:NotBlank
    val userImageUrl: String,
    val socialType: SocialType
)
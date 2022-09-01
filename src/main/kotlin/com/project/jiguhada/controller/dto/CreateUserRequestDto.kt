package com.project.jiguhada.controller.dto

import com.project.jiguhada.util.SocialType

data class CreateUserRequestDto(
    val username: String,
    val nickname: String,
    val password: String,
    val userImageUrl: String,
    val socialType: SocialType
)
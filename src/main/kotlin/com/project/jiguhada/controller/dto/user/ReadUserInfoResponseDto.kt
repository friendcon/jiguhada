package com.project.jiguhada.controller.dto.user

import com.project.jiguhada.util.SocialType

data class ReadUserInfoResponseDto(
    val username: String,
    val nickname: String,
    val imgUrl: String,
    val socialType: SocialType
)

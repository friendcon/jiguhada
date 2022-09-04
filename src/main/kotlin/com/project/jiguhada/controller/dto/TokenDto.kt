package com.project.jiguhada.controller.dto

data class TokenDto(
    val accessToken: String? = null,
    val refreshToken: String? = null,
    val userid: Long,
    val nickname: String,
    val accessTokenExpiredDate: String
)

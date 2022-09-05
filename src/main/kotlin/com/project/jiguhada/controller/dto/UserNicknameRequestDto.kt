package com.project.jiguhada.controller.dto

data class UserNicknameRequestDto(
    val accesstoken: String,
    val userid: Long,
    val nickname: String
)

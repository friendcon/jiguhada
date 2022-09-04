package com.project.jiguhada.controller.dto

import java.util.Date

data class TokenDto(
    val token: String? = null,
    val userid: Long,
    val nickname: String,
    val expiredDate: Date
)

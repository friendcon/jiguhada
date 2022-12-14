package com.project.jiguhada.controller.dto.board

import com.querydsl.core.annotations.QueryProjection

data class BoardLikeResponseDto @QueryProjection constructor(
    val likeId: Long,
    val userId: Long,
    val username: String,
    val nickname: String,
    val userImgUrl: String
)


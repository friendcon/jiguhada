package com.project.jiguhada.controller.dto.board.refactor

import com.querydsl.core.annotations.QueryProjection

data class BoardLikeItem @QueryProjection constructor(
    val likeId: Long,
    val userId: Long,
    val username: String,
    val nickname: String
)

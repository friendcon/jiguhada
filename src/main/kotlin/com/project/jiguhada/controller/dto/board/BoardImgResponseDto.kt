package com.project.jiguhada.controller.dto.board

import com.querydsl.core.annotations.QueryProjection

data class BoardImgResponseDto @QueryProjection constructor(
    val imgId: Long,
    val imgUrl: String
)

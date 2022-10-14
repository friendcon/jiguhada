package com.project.jiguhada.controller.dto.board

import com.project.jiguhada.util.BOARD_CATEGORY
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class BoardListItemResponse @QueryProjection constructor(
    val category: BOARD_CATEGORY,
    val boardId: Long,
    val boardTitle: String,
    val writer: String,
    val userId: Long,
    val username: String,
    val userInfoPublic: String,
    val createDate: LocalDateTime,
    val viewCount: Long,
    val likeCount: Long,
    val commentCount: Long
)

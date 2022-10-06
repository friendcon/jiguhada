package com.project.jiguhada.controller.dto.boardcomment

import com.project.jiguhada.util.BOARD_CATEGORY
import com.project.jiguhada.util.IS_USER_INFO_PUBLIC
import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class BoardCommentItem @QueryProjection constructor(
    val boardId: Long,
    val boardTitle: String,
    val boardCategory: BOARD_CATEGORY,
    val commentId: Long,
    val commentContent: String,
    val commentCount: Long,
    val nickname: String,
    val userId: Long?,
    val userImg: String?,
    val userInfoPublic: IS_USER_INFO_PUBLIC,
    val commentCreateDate: LocalDateTime,
    val commentUpdateDate: LocalDateTime
)

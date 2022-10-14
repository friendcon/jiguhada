package com.project.jiguhada.controller.dto.board.refactor

import com.project.jiguhada.util.IS_USER_INFO_PUBLIC
import java.time.LocalDateTime

data class BoardResponse(
    val boardId: Long,
    val title: String,
    val content: String,
    val viewCount: Long,
    val boardCategory: String,
    val username: String,
    val userId: Long,
    val userImgUrl: String,
    val userInfoPublic: IS_USER_INFO_PUBLIC,
    val nickname: String,
    val commentCount: Long,
    val likeCount: Long,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime
)

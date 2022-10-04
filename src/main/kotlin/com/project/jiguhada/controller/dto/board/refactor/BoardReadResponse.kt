package com.project.jiguhada.controller.dto.board.refactor

import java.time.LocalDateTime

data class BoardReadResponse(
    val boardId: Long,
    val title: String,
    val content: String,
    val viewCount: Long,
    val boardCategory: String,
    val username: String,
    val userId: Long,
    val userImgUrl: String,
    val nickname: String,
    val commentCount: Long,
    val likeCount: Long,
    val createDate: LocalDateTime,
    val updateDate: LocalDateTime
)

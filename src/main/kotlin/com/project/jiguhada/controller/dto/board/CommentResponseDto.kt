package com.project.jiguhada.controller.dto.board

import java.time.LocalDateTime

data class CommentResponseDto(
    val commentId: Long,
    val username: String,
    val nickname: String,
    val content: String,
    val createdDate: LocalDateTime
)
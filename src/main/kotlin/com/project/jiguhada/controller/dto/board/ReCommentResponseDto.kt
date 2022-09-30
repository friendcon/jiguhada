package com.project.jiguhada.controller.dto.board

import java.time.LocalDateTime

data class ReCommentResponseDto(
    val parentCommentId: Long?,
    val commentId: Long,
    val username: String,
    val userUrl: String,
    val nickname: String,
    val content: String,
    val createdDate: LocalDateTime,
    val updatedDate: LocalDateTime
)

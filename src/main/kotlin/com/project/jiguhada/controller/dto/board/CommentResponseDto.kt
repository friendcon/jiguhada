package com.project.jiguhada.controller.dto.board

data class CommentResponseDto(
    val commentId: Long,
    val username: String,
    val nickname: String,
    val content: String
)
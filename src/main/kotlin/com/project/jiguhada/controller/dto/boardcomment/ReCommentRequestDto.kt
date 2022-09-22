package com.project.jiguhada.controller.dto.boardcomment

data class ReCommentRequestDto(
    val boardId: Long,
    val parentCommentId: Long,
    val content: String
)

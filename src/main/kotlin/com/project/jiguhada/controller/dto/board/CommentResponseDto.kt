package com.project.jiguhada.controller.dto.board

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class CommentResponseDto @QueryProjection constructor(
    val commentId: Long,
    val username: String,
    val userId: Long,
    val userUrl: String,
    val nickname: String,
    val content: String,
    val createdDate: LocalDateTime,
    val parentComment: Long?,
    val childComment: List<ReCommentResponseDto>
)
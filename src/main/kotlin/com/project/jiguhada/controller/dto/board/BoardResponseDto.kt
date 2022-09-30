package com.project.jiguhada.controller.dto.board

import com.querydsl.core.annotations.QueryProjection
import java.time.LocalDateTime

data class BoardResponseDto @QueryProjection constructor(
    val boardId: Long,
    val title: String,
    val content: String,
    val viewCount: Long,
    val boardCategory: String,
    val username: String,
    val userId: Long,
    val userImgUrl: String,
    val nickname: String,
    val createDate: LocalDateTime,
    val commentList: List<CommentResponseDto>,
    val likeList: List<BoardLikeResponseDto>,
    val imgList: List<BoardImgResponseDto>
) {

}
package com.project.jiguhada.controller.dto.boardcomment

data class BoardCommentList(
    val totalCommentCount: Long,
    val currentPage: Long,
    val totalPage: Long,
    val commentList: List<BoardCommentItem>
)

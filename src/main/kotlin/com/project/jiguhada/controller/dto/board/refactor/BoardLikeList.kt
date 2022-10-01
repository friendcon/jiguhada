package com.project.jiguhada.controller.dto.board.refactor

data class BoardLikeList(
    val totalLikeCount: Long,
    val currentPage: Long,
    val totalPage: Long,
    val likeList: List<BoardLikeItem>
)

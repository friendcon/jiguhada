package com.project.jiguhada.controller.dto.board

data class BoardListResponse (
    val totalBoardCount: Long,
    val currentPage: Long,
    val totalPage: Long,
    val boardItemList: List<BoardListItemResponse>
)

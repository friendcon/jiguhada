package com.project.jiguhada.controller.dto.board

import com.project.jiguhada.util.BOARD_CATEGORY

data class BoardUpdateRequestDto(
    val boardId: Long,
    val title: String,
    val content: String,
    val boardCategory: BOARD_CATEGORY,
    val boardImg: List<BoardImgRequestDto>,
    val deleteImg: List<BoardImgRequestDto>
)

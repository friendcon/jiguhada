package com.project.jiguhada.controller.dto.board

import com.project.jiguhada.util.BOARD_CATEGORY

data class BoardModifyRequestDto(
    val title: String,
    val content: String,
    val category: BOARD_CATEGORY,
    val jupdateImg: List<BoardImgRequestDto>,
    val deleteImg: List<BoardImgRequestDto>
)

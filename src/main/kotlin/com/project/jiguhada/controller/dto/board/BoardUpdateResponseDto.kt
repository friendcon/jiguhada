package com.project.jiguhada.controller.dto.board

data class BoardUpdateResponseDto(
    val title: String,
    val content: String,
    val boardCategory: String,
    val nickname: String,
    val imgList: List<BoardImgResponseDto>
)

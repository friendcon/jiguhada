package com.project.jiguhada.controller.dto.board

data class BoardResponseDto(
    val boardId: Long,
    val title: String,
    val content: String,
    val viewCount: Long,
    val boardCategory: String,
    val username: String,
    val nickname: String,
    val commentList: List<CommentResponseDto>,
    val likeList: List<BoardLikeResponseDto>,
    val imgList: List<String>
) {

}

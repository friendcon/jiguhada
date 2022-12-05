package com.project.jiguhada.controller.dto.boardcomment

/**
 * BoardLikeItem 이랑 합치면 될 것 같은데..
 */
data class BoardCommentLikeItem(
    val likeId: Long,
    val username: String,
    val usernickname: String,
    val userId: Long,
    val userImgUrl: String,
    val userInfoPublic: String
)

package com.project.jiguhada.controller.dto

data class UserPasswordRequestDto(
    val accesstoken: String,
    val userid: Long,
    val nowpassword: String,
    val newpassword: String
)

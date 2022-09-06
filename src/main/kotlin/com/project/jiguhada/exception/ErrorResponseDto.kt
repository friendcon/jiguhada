package com.project.jiguhada.exception

import com.project.jiguhada.util.ERRORCODE

data class ErrorResponseDto(
    val errorCode: ERRORCODE,
    val message: String?
)
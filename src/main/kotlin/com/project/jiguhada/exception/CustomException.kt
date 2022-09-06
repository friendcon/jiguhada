package com.project.jiguhada.exception

class UsernameNotFoundException(message: String?) : RuntimeException() {
    override val message: String? = message
}

class UserInfoDuplicateException(message: String?): RuntimeException() {
    override val message: String? = message
}

class CustomException(message: String?): RuntimeException() {
    override val message: String? = message
}

class UserNowPasswordNotMatchException(message: String?): RuntimeException() {
    override val message: String? = message
}

class UnauthorizedRequestException(message: String?): RuntimeException() {
    override val message: String? = message
}
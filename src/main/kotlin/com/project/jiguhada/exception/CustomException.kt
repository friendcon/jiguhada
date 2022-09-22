package com.project.jiguhada.exception

class UsernameNotFoundException(override val message: String) : RuntimeException()
class UserNicknameDuplicateException(override val message: String): RuntimeException()
class UserIdDuplicateException(override val message: String): RuntimeException()
class CustomException(override val message: String): RuntimeException()
class UserNowPasswordNotMatchException(override val message: String): RuntimeException()
class UnauthorizedRequestException(override val message: String): RuntimeException()
class FailToUploadImgException(override val message: String): RuntimeException()

class RequestBoardIdNotMatched(override val message: String): RuntimeException()

class LimitFileCountException(override val message: String): RuntimeException()

class UserAlreadyLikeBoard(override val message: String): RuntimeException()
package com.project.jiguhada.exception

import com.fasterxml.jackson.databind.RuntimeJsonMappingException

class ClientBadRequest(override val message: String): RuntimeException()
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

class UserAlreadyChallengeMemberException(override val message: String): RuntimeException()

class ChallengeJoinCountException(override val message: String): RuntimeException()
class ChallengeJoinEndException(override val message: String): RuntimeException()

class ChallengeException(override val message: String): RuntimeException()
class CantAuthException(override val message: String): RuntimeException()

class AlreadyAuthException(override val message: String): RuntimeException()
class UserInfoIsPrivateException(override val message: String): RuntimeException()
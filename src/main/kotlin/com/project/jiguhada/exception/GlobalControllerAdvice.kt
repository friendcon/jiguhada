package com.project.jiguhada.exception

import com.fasterxml.jackson.databind.exc.InvalidFormatException
import com.project.jiguhada.util.ERRORCODE
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.MalformedJwtException
import io.jsonwebtoken.UnsupportedJwtException
import io.jsonwebtoken.security.SignatureException
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.BadCredentialsException
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice


// restcontroller 에서 발생하는 exception 여기서 처리
@RestControllerAdvice
class GlobalControllerAdv0ice {
    @ExceptionHandler(BadCredentialsException::class)
    fun badCredentialException(e: BadCredentialsException): ResponseEntity<ErrorResponseDto>{
        return ResponseEntity(ErrorResponseDto(ERRORCODE.ID_PASSWORD_NOTMATCH, "인증정보가 일치하지 않습니다"), HttpStatus.BAD_REQUEST)
    }

    /*@ExceptionHandler(NullPointerException::class)
    fun nullPointerException(e: NullPointerException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.REQUEST_NOT_INCLUDE_TOKEN, e.message), HttpStatus.BAD_REQUEST)
    }*/
    @ExceptionHandler(SecurityException::class)
    fun securityException(e: SecurityException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.INCORRECT_JWT_SIGNATURE, "잘못된 JWT 서명입니다"), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(MalformedJwtException::class)
    fun malformedJwtException(e: MalformedJwtException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.MALFORMED_JWT_SIGNATURE, "잘못된 JWT 서명입니다"), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(SignatureException::class)
    fun signatureException(e: SignatureException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.INCORRECT_JWT_SIGNATURE, "잘못된 JWT 서명입니다"), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(UsernameNotFoundException::class)
    fun usernameNotFoundException(e: UsernameNotFoundException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.NOT_EXITS_ID, e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun methodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.INSUFFICIENT_REQUEST, e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNicknameDuplicateException::class)
    fun userNicknameDuplicateException(e: UserNicknameDuplicateException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.DUPLICATE_NICKNAME, e.message), HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler(UserIdDuplicateException::class)
    fun userIdDuplicateException(e: UserIdDuplicateException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.DUPLICATE_ID, e.message), HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler(CustomException::class)
    fun customException(e: CustomException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.NOW_PASSWORD_NOTMATCH, e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserNowPasswordNotMatchException::class)
    fun userNowPasswordNotMatchException(e: UserNowPasswordNotMatchException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.NOW_PASSWORD_NOTMATCH, e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(ExpiredJwtException::class)
    fun expiredJwtException(e: ExpiredJwtException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.EXPIRE_ACCESS_TOKEN, "만료된 토큰입니다. 다시 로그인해주세요."), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(UnauthorizedRequestException::class)
    fun unauthorizedRequestException(e: UnauthorizedRequestException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.UNAUTHORIZED_REQUEST, e.message), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(UnsupportedJwtException::class)
    fun unSupportedJwtException(e: UnsupportedJwtException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.NOT_SUPPORTED_TOKEN, "지원되지 않은 JWT 토큰입니다"), HttpStatus.BAD_REQUEST)
    }
    @ExceptionHandler(FailToUploadImgException::class)
    fun failToUploadImgException(e: FailToUploadImgException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.FAIL_TO_UPLOAD_IMG, e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(RequestBoardIdNotMatched::class)
    fun requestBoardIdNotMatched(e: RequestBoardIdNotMatched): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.REQUEST_BOARDID_NOT_MATCHED, e.message), HttpStatus.UNAUTHORIZED)
    }

    @ExceptionHandler(IllegalArgumentException::class)
    fun illegalArgumentException(e: IllegalArgumentException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.KEY_NOT_EXIST, "Key 요청이 올바르지 않습니다"), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(LimitFileCountException::class)
    fun limitFileCountException(e: LimitFileCountException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.LIMIT_FILE_COUNT, e.message), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(InvalidFormatException::class)
    fun invalidFormatException(e: InvalidFormatException): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.INVALID_FORMAT_REQUEST, "잘못된 요청"), HttpStatus.BAD_REQUEST)
    }

    @ExceptionHandler(UserAlreadyLikeBoard::class)
    fun userAlreadyLikeBoard(e:UserAlreadyLikeBoard): ResponseEntity<ErrorResponseDto> {
        return ResponseEntity(ErrorResponseDto(ERRORCODE.USER_ALREADY_LIKE, e.message), HttpStatus.BAD_REQUEST)
    }
}

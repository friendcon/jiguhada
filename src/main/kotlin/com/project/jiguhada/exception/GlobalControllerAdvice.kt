package com.project.jiguhada.exception

import com.project.jiguhada.util.ERRORCODE
import io.jsonwebtoken.ExpiredJwtException
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

}

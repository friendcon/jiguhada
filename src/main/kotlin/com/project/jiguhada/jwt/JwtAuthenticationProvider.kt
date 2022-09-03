package com.project.jiguhada.jwt

import com.project.jiguhada.service.JwtUserDetailsService
import io.jsonwebtoken.*
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.userdetails.User
import org.springframework.stereotype.Component
import org.springframework.util.StringUtils
import java.security.Key
import java.util.*
import javax.servlet.http.HttpServletRequest

@Component
class JwtAuthenticationProvider(
    @Value("\${jwt.secret}")
    val secret: String,
    @Value("\${jwt.token-validity-in-seconds}")
    val tokenValidityInSeconds: Long,
    private val jwtUserDetailsService: JwtUserDetailsService
): InitializingBean {
    private val logger = LoggerFactory.getLogger(JwtAuthenticationProvider::class.java)
    private val tokenValidityInMilliseconds: Long
    private var key: Key? = null

    init {
        tokenValidityInMilliseconds = tokenValidityInSeconds * 1000
    }

    companion object {
        const val AUTHORITIES_KEY = "auth"
        const val USERNAME_KEY = "username"
        const val USERID_KEY = "userid"
        const val NICKNAME_KEY = "nickname"
        const val AUTHORIZATION_HEADER = "Authorization"
    }

    override fun afterPropertiesSet() {
        key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))
    }

    // 인증을 위한 토큰 추출
    fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        return if(StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            bearerToken.substring(7)
        } else null
    }

    fun getUsername(jwt: String): String {
        return Jwts
            .parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(jwt)
            .body["username"].toString()
    }

    // security context에 인증정보를 저장하기 위해 Authentication 객체 생성
    fun createAuthentication(jwt: String): Authentication {
        val username = getUsername(jwt)
        val userDetails = jwtUserDetailsService.loadUserByUsername(username)
        val authorities = userDetails.authorities
        val principal = User(username, "", authorities)

        return UsernamePasswordAuthenticationToken(principal, jwt, authorities)
    }

    fun createToken(authentication: Authentication): String {
        val claims = Jwts.claims()

        val authorities = authentication.authorities.map {
            it.authority
        }.joinToString(",")

        val username = authentication.name
        val userResponse = jwtUserDetailsService.loadUserByUsername(username)

        val jwtUserDetails = userResponse as JwtUserDetails

        claims[USERNAME_KEY] = username
        claims[USERID_KEY] = jwtUserDetails.id
        claims[NICKNAME_KEY] = jwtUserDetails.nickname
        claims[AUTHORITIES_KEY] = authorities

        val validity = Date(Date().time + tokenValidityInMilliseconds)

        return Jwts.builder()
            .setIssuedAt(Date()) // 발급일
            .setClaims(claims) // 데이터
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(validity) // 만료일
            .compact()
    }

    // 사용자가 요청한 토큰의 유효성 검사 : 서버가 생성한 토큰이 맞는지?
    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        } catch (e: SecurityException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: MalformedJwtException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            logger.info("지원되지 않는 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            logger.info("JWT 토큰이 잘못되었습니다.")
        }
        return false
    }
}
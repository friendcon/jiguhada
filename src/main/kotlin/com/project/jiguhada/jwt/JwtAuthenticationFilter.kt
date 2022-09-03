package com.project.jiguhada.jwt

import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.GenericFilterBean
import javax.servlet.FilterChain
import javax.servlet.ServletRequest
import javax.servlet.ServletResponse
import javax.servlet.http.HttpServletRequest

class JwtAuthenticationFilter(
    private val jwtAuthenticationProvider: JwtAuthenticationProvider
): GenericFilterBean() {
    companion object {
        private val logger = LoggerFactory.getLogger(JwtAuthenticationFilter::class.java)
    }

    /**
     * 모든 request의 JWT Token 을 인증하는 필터
     */
    override fun doFilter(request: ServletRequest?, response: ServletResponse?, filterChain: FilterChain?) {
        val httpServletRequest = request as HttpServletRequest
        val jwt = jwtAuthenticationProvider.resolveToken(httpServletRequest) // 요청에서 jwt 추출
        val requestURI = request.requestURI

        // jwt 의 유효성 검증
        if(jwt != null && jwtAuthenticationProvider.validateToken(jwt)) {
            val authentication = jwtAuthenticationProvider.createAuthentication(jwt)
            SecurityContextHolder.getContext().authentication = authentication
            Companion.logger.debug("Security Context에 '{}' 인증 정보를 저장했습니다, uri: {}", authentication.name, requestURI)
        } else {
            Companion.logger.debug("유효한 JWT 토큰이 없습니다, uri: {}", requestURI)
        }

        filterChain?.doFilter(request, response)
    }
}
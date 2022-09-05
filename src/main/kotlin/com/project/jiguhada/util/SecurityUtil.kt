package com.project.jiguhada.util

import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder

object SecurityUtil {
    private val logger = LoggerFactory.getLogger(SecurityUtil::class.java)
    val currentUsername: String
        get() {
            val authentication = SecurityContextHolder.getContext().authentication

            if(authentication == null || authentication.name == null) {
                logger.debug("Security Context에 인증 정보가 없습니다.")
            }
            return authentication.name
        }
}
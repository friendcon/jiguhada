
package com.project.jiguhada.config

import com.project.jiguhada.jwt.JwtAuthenticationFilter
import com.project.jiguhada.jwt.JwtAuthenticationProvider
import org.springframework.security.config.annotation.SecurityConfigurerAdapter
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.DefaultSecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class JwtSecurityConfig(private val tokenProvider: JwtAuthenticationProvider) :
    SecurityConfigurerAdapter<DefaultSecurityFilterChain?, HttpSecurity>() {
    override fun configure(http: HttpSecurity) {
        http.addFilterBefore(
            JwtAuthenticationFilter(tokenProvider),
            UsernamePasswordAuthenticationFilter::class.java
        )
    }
}

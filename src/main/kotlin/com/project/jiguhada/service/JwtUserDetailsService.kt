package com.project.jiguhada.service

import com.project.jiguhada.jwt.JwtUserDetails
import com.project.jiguhada.repository.user.UserEntityRepository
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
class JwtUserDetailsService(
    private val userEntityRepository: UserEntityRepository
): UserDetailsService {

    @Transactional
    override fun loadUserByUsername(username: String?): UserDetails {
        return getJwtUserDetails(username)
    }

    fun getJwtUserDetails(username: String?): JwtUserDetails {
        val response = userEntityRepository.findByUsername(username).get()
        return JwtUserDetails(
            response.id!!,
            response.username,
            response.nickname,
            response.password,
            response.userImageUrl,
            response.isenabled,
            response.roles.map {
                SimpleGrantedAuthority(it.roleName.toString())
            }.toMutableList()
        )
    }
}
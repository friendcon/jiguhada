package com.project.jiguhada.jwt

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class JwtUserDetails(
    val id: Long,
    val userName: String,
    val nickname: String,
    val passWord: String,
    val roles: MutableList<GrantedAuthority> = mutableListOf<GrantedAuthority>()
): UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return roles
    }

    override fun getPassword(): String {
        return passWord
    }

    override fun getUsername(): String {
        return userName
    }

    /**
     * 계정만료여부
     * true : 만료 x
     * false : 만료
     */
    override fun isAccountNonExpired(): Boolean {
        return true
    }

    /**
     * 계정잠김여부
     * true : 잠김 x
     * false : 잠김
     */
    override fun isAccountNonLocked(): Boolean {
        return true
    }

    /**
     * 비밀번호만료여부
     * true : 만료 x
     * false : 만료
     */
    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    /**
     * 사용자 활성화 여부
     * true : 활성화
     * false : 비활성화
     */
    override fun isEnabled(): Boolean {
        return true
    }
}
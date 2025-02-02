package com.study.springboot_security_jwt.login

import com.study.springboot_security_jwt.entity.UserEntity
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails

class CustomUserDetails(
    val user: UserEntity
): UserDetails {
    override fun getAuthorities(): MutableCollection<out GrantedAuthority> = user.role.split(",").map{SimpleGrantedAuthority(it)}.toMutableList()

    override fun getPassword(): String = user.password

    override fun getUsername(): String = user.username

    override fun isAccountNonExpired(): Boolean {
        return super.isAccountNonExpired()
    }

    override fun isAccountNonLocked(): Boolean {
        return super.isAccountNonLocked()
    }

    override fun isCredentialsNonExpired(): Boolean {
        return super.isCredentialsNonExpired()
    }

    override fun isEnabled(): Boolean {
        return super.isEnabled()
    }
}
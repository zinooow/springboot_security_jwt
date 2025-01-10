package com.study.springboot_security_jwt.login

import com.study.springboot_security_jwt.entity.UserEntity
import com.study.springboot_security_jwt.repository.UserRepository
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class CustomUserDetailsService(
    private val userRepository: UserRepository,
): UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: UserEntity = userRepository.findByUsername(username)?: throw UsernameNotFoundException("User not found: $username")

        return CustomUserDetails(user = user)
    }

}
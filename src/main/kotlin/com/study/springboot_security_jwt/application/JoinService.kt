package com.study.springboot_security_jwt.application

import com.study.springboot_security_jwt.dto.JoinDTO
import com.study.springboot_security_jwt.entity.UserEntity
import com.study.springboot_security_jwt.repository.UserRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service

@Service
class JoinService(
    val userRepository: UserRepository,
    val bCryptPasswordEncoder: BCryptPasswordEncoder,
) {

    fun join(joinDTO: JoinDTO) {

        if(userRepository.existsByUsername(joinDTO.username)) return

        userRepository.save(
            UserEntity(
                username = joinDTO.username,
                password = bCryptPasswordEncoder.encode(joinDTO.password),
                role = "ROLE_USER"
            )
        )
    }

}
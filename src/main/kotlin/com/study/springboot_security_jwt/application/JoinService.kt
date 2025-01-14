package com.study.springboot_security_jwt.application

import com.study.springboot_security_jwt.dto.JoinDTO
import com.study.springboot_security_jwt.entity.UserEntity
import com.study.springboot_security_jwt.repository.UserRepository
import org.springframework.dao.DuplicateKeyException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class JoinService(
    val userRepository: UserRepository,
    val passwordEncoder: PasswordEncoder,
) {

    fun join(joinDTO: JoinDTO) {

        if(userRepository.existsByUsername(joinDTO.username)) throw DuplicateKeyException("User Already Exists")

        userRepository.save(
            UserEntity(
                username = joinDTO.username,
                password = passwordEncoder.encode(joinDTO.password),
                role = "ROLE_USER"
            )
        )
    }

}
package com.study.springboot_security_jwt.repository

import com.study.springboot_security_jwt.entity.UserEntity
import org.springframework.data.jpa.repository.JpaRepository

interface UserRepository:JpaRepository<UserEntity, Int> {
    fun existsByUsername(username:String):Boolean
    fun findByUsername(username:String):UserEntity?
}
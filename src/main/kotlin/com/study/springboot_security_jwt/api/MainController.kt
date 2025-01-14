package com.study.springboot_security_jwt.api

import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class MainController {
    @GetMapping("/")
    fun mainP():String {
        val name:String = SecurityContextHolder.getContext().authentication.name
        return "Main Controller : $name"
    }

}
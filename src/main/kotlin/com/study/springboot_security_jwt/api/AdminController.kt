package com.study.springboot_security_jwt.api

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class AdminController {
    @GetMapping("/admin")
    fun admin() = "Admin Controller"
}
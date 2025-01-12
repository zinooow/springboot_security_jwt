package com.study.springboot_security_jwt.api

import com.study.springboot_security_jwt.application.JoinService
import com.study.springboot_security_jwt.dto.JoinDTO
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

@RestController
class JoinController (
    val joinService: JoinService
){

    @PostMapping("/join")
    fun join(joinDTO: JoinDTO): String{
        joinService.join(joinDTO)
        return "${joinDTO.username} OK"
    }

}
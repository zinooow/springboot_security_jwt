package com.study.springboot_security_jwt.jwt

import com.study.springboot_security_jwt.entity.UserEntity
import com.study.springboot_security_jwt.login.CustomUserDetails
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.filter.OncePerRequestFilter

class JWTFilter(
    private val jwtUtil: JWTUtil
) : OncePerRequestFilter() {

    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {

        val authorization: String? = request.getHeader("Authorization")

        if(authorization == null || !authorization.startsWith("Bearer ")){
            println("Token is null")
            filterChain.doFilter(request, response)
            return
        }

        println("authorization now");
        val token:String = authorization.split(" ")[1]

        if(jwtUtil.isExpired(token)){
            println("Token is expired")
            filterChain.doFilter(request, response)
            return
        }

        val username:String = jwtUtil.getUsername(token)
        val role:String = jwtUtil.getRole(token)

        val userEntity = UserEntity(username = username, password = "temppassword", role =  role)

        val userDetails:CustomUserDetails = CustomUserDetails(userEntity)
        val authToken:Authentication =  UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)

        SecurityContextHolder.getContext().authentication = authToken

        filterChain.doFilter(request, response)

    }
}
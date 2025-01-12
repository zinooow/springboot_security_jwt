package com.study.springboot_security_jwt.jwt

import com.study.springboot_security_jwt.login.CustomUserDetails
import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.GrantedAuthority
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

class LoginFilter(
    private val authenticationManager: AuthenticationManager,
    private val jwtUtil: JWTUtil
    ) : UsernamePasswordAuthenticationFilter() {

    override fun attemptAuthentication(request: HttpServletRequest?, response: HttpServletResponse?): Authentication {
        val username:String = obtainUsername(request).toString()
        val password:String = obtainPassword(request).toString()
        val authToken: UsernamePasswordAuthenticationToken = UsernamePasswordAuthenticationToken(username, password, null)

        /*
        * obtainUsername 메서드를 통해 request로 요청된 username, password 를 가져와서
        * UsernamePasswordAuthenticationToken 객체를 생성하여 AuthenticationManager 클래스의 authenticate 메서드를 사용하여 Authentication 객체를 반환받는다
        * */
        return authenticationManager.authenticate(authToken)
    }

    override fun successfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        chain: FilterChain?,
        authResult: Authentication
    ) {
        val userDetails: CustomUserDetails = authResult.principal as CustomUserDetails
        val username: String = userDetails.username

        val authority: MutableCollection<out GrantedAuthority> = userDetails.authorities
        val itr: Iterator<GrantedAuthority> = authority.iterator()
        val auth: GrantedAuthority = itr.next()
        val role: String = auth.authority

        val token: String = jwtUtil.generateToken(username, role, 60*60*10L)

        response.addHeader("Authorization", "Bearer $token")
    }

    override fun unsuccessfulAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse,
        failed: AuthenticationException
    ) {
        response.status = 401
    }
}
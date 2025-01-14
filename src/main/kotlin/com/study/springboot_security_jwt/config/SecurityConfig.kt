package com.study.springboot_security_jwt.config

import com.study.springboot_security_jwt.jwt.JWTFilter
import com.study.springboot_security_jwt.jwt.JWTUtil
import com.study.springboot_security_jwt.jwt.LoginFilter
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration
import org.springframework.web.cors.CorsConfigurationSource
import org.springframework.web.cors.UrlBasedCorsConfigurationSource

@Configuration
@EnableWebSecurity
class SecurityConfig (
    private val authenticationConfiguration: AuthenticationConfiguration,
    private val jwtUtil: JWTUtil
){

    @Bean
    fun filterChain(http: HttpSecurity, corsConfigurationSource: CorsConfigurationSource): SecurityFilterChain {
        http
            .csrf { auth -> auth.disable() }
            .formLogin { auth -> auth.disable() }
            .httpBasic { auth -> auth.disable() }
            .authorizeHttpRequests { auth -> auth
                .requestMatchers("/login","/","/join").permitAll()
//                .requestMatchers("/admin").hasRole("ADMIN")
                .anyRequest().authenticated()
            }
            .sessionManagement { session -> session
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
            }
            .cors {}
            .addFilterBefore(JWTFilter(jwtUtil), LoginFilter::class.java)
            .addFilterAt(LoginFilter(authenticationManager(authenticationConfiguration),jwtUtil), UsernamePasswordAuthenticationFilter::class.java)
        return http.build()
    }

    @Bean
    fun bCryptPasswordEncoder(): BCryptPasswordEncoder = BCryptPasswordEncoder()

    //AuthenticationManager Bean 등록
    @Bean
    fun authenticationManager(configuration: AuthenticationConfiguration): AuthenticationManager {
        return configuration.authenticationManager
    }

    @Bean
    fun corsConfigurationSource(): CorsConfigurationSource {
        val configuration = CorsConfiguration()
        configuration.allowedOrigins = listOf("*")
        configuration.allowedMethods = listOf("GET", "POST", "PUT", "DELETE")
        configuration.allowCredentials = true
        configuration.allowedHeaders = listOf("*")
        configuration.exposedHeaders = listOf("Authorization")
        configuration.maxAge = 3600L

        val source = UrlBasedCorsConfigurationSource()
        source.registerCorsConfiguration("/**", configuration)
        return source
    }

}
package com.study.springboot_security_jwt.jwt

import io.jsonwebtoken.Jwts
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import java.nio.charset.StandardCharsets
import java.util.*
import javax.crypto.SecretKey
import javax.crypto.spec.SecretKeySpec

@Component
class JWTUtil(
    @Value("\${spring.jwt.secret}")
    private val secret: String,
) {
    private val secretKey: SecretKey = SecretKeySpec(secret.toByteArray(StandardCharsets.UTF_8), Jwts.SIG.HS256.key().build().algorithm)

    fun generateToken(
        username: String,
        role: String,
        expiredMs: Long
    ): String {
        return Jwts.builder()
            .claim("username",username)
            .claim("role",role)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis()+expiredMs))
            .signWith(secretKey)
            .compact()
    }

    fun getUsername(token:String): String{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.get("username",String::class.java)
    }

    fun getRole(token:String): String{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.get("username",String::class.java)
    }

    fun isExpired(token:String): Boolean{
        return Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.expiration.before(Date())
    }


}
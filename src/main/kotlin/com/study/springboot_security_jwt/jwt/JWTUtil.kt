package com.study.springboot_security_jwt.jwt

import io.jsonwebtoken.Jwts
import io.jsonwebtoken.io.Decoders
import io.jsonwebtoken.security.Keys
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

    private val key = Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret))

    fun generateToken(
        username: String,
        role: String,
        expiredMs: Long
    ): String = Jwts.builder()
            .claim("username",username)
            .claim("role",role)
            .issuedAt(Date(System.currentTimeMillis()))
            .expiration(Date(System.currentTimeMillis()+expiredMs))
            .signWith(secretKey)
            .compact()

    fun getUsername(token:String): String = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.get("username",String::class.java)

    fun getRole(token:String): String = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.get("role",String::class.java)

    fun isExpired(token:String): Boolean = Jwts.parser().verifyWith(secretKey).build().parseSignedClaims(token).payload.expiration.before(Date())

}
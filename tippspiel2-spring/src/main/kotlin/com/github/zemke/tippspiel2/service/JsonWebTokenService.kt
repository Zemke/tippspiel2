package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.authentication.AuthenticatedUser
import com.github.zemke.tippspiel2.core.properties.AuthenticationProperties
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.function.Function
import javax.servlet.http.HttpServletRequest
import java.time.Instant
import java.util.Date

@Component
class JsonWebTokenService(
        @Autowired() private val authenticationProperties: AuthenticationProperties
) {

    private val clock = DefaultClock.INSTANCE

    fun assertToken(request: HttpServletRequest): String? =
            request.getHeader(authenticationProperties.jwt.header)?.substring(7)

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val user = userDetails as AuthenticatedUser
        val email = getSubjectFromToken(token)
        val created = getIssuedAtDateFromToken(token)
        // TODO Why is this commented out?
        //final Date expiration = getExpirationDateFromToken(token);
        return (email == user.username
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, user.lastPasswordResetDate))
    }

    fun generateToken(authenticatedUser: AuthenticatedUser): String {
        val claims = mapOf(
                Pair("id", authenticatedUser.id),
                Pair("firstName", authenticatedUser.firstName),
                Pair("lastName", authenticatedUser.lastName),
                Pair("email", authenticatedUser.email),
                Pair("roles", authenticatedUser.roles.map { it.name.unPrefixed() }))
        return doGenerateToken(claims, authenticatedUser.username)
    }

    fun canTokenBeRefreshed(token: String, lastPasswordReset: Instant?): Boolean {
        val created = getIssuedAtDateFromToken(token)
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && !isTokenExpired(token)
    }

    fun refreshToken(token: String): String {
        val createdDate = clock.now()
        val expirationDate = calculateExpirationDate(createdDate)

        val claims = getAllClaimsFromToken(token)
        claims.issuedAt = createdDate
        claims.expiration = expirationDate

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, authenticationProperties.jwt.secret)
                .compact()
    }

    fun getSubjectFromToken(token: String): String {
        return getClaimFromToken(token, Function { it.subject })
    }

    fun getIdFromToken(token: String): Long {
        return getClaimFromToken<Int>(token, Function { it["id"] as Int }).toLong()
    }

    private fun getIssuedAtDateFromToken(token: String): Date {
        return getClaimFromToken(token, Function { it.issuedAt })
    }

    private fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Function { it.expiration })
    }

    private fun <T> getClaimFromToken(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(authenticationProperties.jwt.secret)
                .parseClaimsJws(token)
                .body
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(clock.now())
    }

    private fun isCreatedBeforeLastPasswordReset(created: Date, lastPasswordReset: Instant?): Boolean {
        return lastPasswordReset != null && created.before(Date.from(lastPasswordReset))
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String): String {
        val createdDate = clock.now()
        val expirationDate = calculateExpirationDate(createdDate)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .setHeaderParam("typ", "JWT")
                .signWith(SignatureAlgorithm.HS512, authenticationProperties.jwt.secret)
                .compact()
    }

    private fun calculateExpirationDate(createdDate: Date): Date {
        return Date(createdDate.time + authenticationProperties.jwt.expiration * 1000)
    }
}

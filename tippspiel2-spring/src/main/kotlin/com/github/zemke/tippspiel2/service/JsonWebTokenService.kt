package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.authentication.AuthenticatedUser
import com.github.zemke.tippspiel2.core.properties.AuthenticationProperties
import com.github.zemke.tippspiel2.view.exception.UnauthorizedException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClock
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import javax.servlet.http.HttpServletRequest

@Component
class JsonWebTokenService(
        @Autowired() private val authenticationProperties: AuthenticationProperties
) {

    companion object {

        internal const val AUDIENCE_MOBILE = "mobile"
        internal const val AUDIENCE_TABLET = "tablet"
    }

    private val clock = DefaultClock.INSTANCE

    @Throws(UnauthorizedException::class)
    fun extractToken(request: HttpServletRequest): String {
        return request.getHeader(authenticationProperties.jwt.header)?.substring(7) ?: throw UnauthorizedException()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val user = userDetails as AuthenticatedUser
        val email = getSubjectFromToken(token)
        val created = getIssuedAtDateFromToken(token)
        //final Date expiration = getExpirationDateFromToken(token);
        return (email == user.username
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, user.lastPasswordResetDate))
    }

    fun generateToken(authenticatedUser: AuthenticatedUser): String {
        val claims = mapOf<String, Any>(
                Pair("firstName", authenticatedUser.firstName),
                Pair("lastName", authenticatedUser.lastName),
                Pair("email", authenticatedUser.email))
        return doGenerateToken(claims, authenticatedUser.username)
    }

    fun canTokenBeRefreshed(token: String, lastPasswordReset: Date?): Boolean {
        val created = getIssuedAtDateFromToken(token)
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!isTokenExpired(token) || ignoreTokenExpiration(token))
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

    private fun getIssuedAtDateFromToken(token: String): Date {
        return getClaimFromToken(token, Function { it.issuedAt })
    }

    private fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Function { it.expiration })
    }

    private fun getAudienceFromToken(token: String): String {
        return getClaimFromToken(token, Function { it.audience })
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

    private fun isCreatedBeforeLastPasswordReset(created: Date, lastPasswordReset: Date?): Boolean {
        return lastPasswordReset != null && created.before(lastPasswordReset)
    }

    private fun ignoreTokenExpiration(token: String): Boolean {
        val audience = getAudienceFromToken(token)
        return AUDIENCE_TABLET == audience || AUDIENCE_MOBILE == audience
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

package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.authentication.AuthenticatedUser
import com.github.zemke.tippspiel2.view.exception.UnauthorizedException
import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClock
import org.springframework.beans.factory.annotation.Value
import org.springframework.mobile.device.Device
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.util.*
import java.util.function.Function
import javax.servlet.http.HttpServletRequest

@Component
class JsonWebTokenService(
        @Value("\${jwt.secret}") private val secret: String,
        @Value("\${jwt.header}") private val header: String,
        @Value("\${jwt.expiration}") private val expiration: Long
) {

    companion object {

//        internal const val CLAIM_KEY_USERNAME = "sub"
//        internal const val CLAIM_KEY_AUDIENCE = "aud"
//        internal const val CLAIM_KEY_CREATED = "iat"

        internal const val AUDIENCE_UNKNOWN = "unknown"
        internal const val AUDIENCE_WEB = "web"
        internal const val AUDIENCE_MOBILE = "mobile"
        internal const val AUDIENCE_TABLET = "tablet"
    }

    private val clock = DefaultClock.INSTANCE

    @Throws(UnauthorizedException::class)
    fun extractToken(request: HttpServletRequest): String {
        return request.getHeader(header)?.substring(7) ?: throw UnauthorizedException()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean {
        val user = userDetails as AuthenticatedUser
        val username = getUsernameFromToken(token)
        val created = getIssuedAtDateFromToken(token)
        //final Date expiration = getExpirationDateFromToken(token);
        return (username == user.username
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, user.lastPasswordResetDate))
    }

    fun generateToken(userDetails: UserDetails, device: Device): String {
        val claims = HashMap<String, Any>()
        return doGenerateToken(claims, userDetails.username, generateAudience(device))
    }

    fun canTokenBeRefreshed(token: String, lastPasswordReset: Date): Boolean {
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
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun getUsernameFromToken(token: String): String {
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
                .setSigningKey(secret)
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

    private fun generateAudience(device: Device): String {
        return when {
            device.isNormal -> AUDIENCE_WEB
            device.isTablet -> AUDIENCE_TABLET
            device.isMobile -> AUDIENCE_MOBILE
            else -> AUDIENCE_UNKNOWN
        }
    }

    private fun ignoreTokenExpiration(token: String): Boolean {
        val audience = getAudienceFromToken(token)
        return AUDIENCE_TABLET == audience || AUDIENCE_MOBILE == audience
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String, audience: String): String {
        val createdDate = clock.now()
        val expirationDate = calculateExpirationDate(createdDate)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    private fun calculateExpirationDate(createdDate: Date): Date {
        return Date(createdDate.time + expiration * 1000)
    }
}

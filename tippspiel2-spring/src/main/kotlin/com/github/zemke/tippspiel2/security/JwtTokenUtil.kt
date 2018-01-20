package com.github.zemke.tippspiel2.security

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.impl.DefaultClock
import org.springframework.beans.factory.annotation.Value
import org.springframework.mobile.device.Device
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Component
import java.io.Serializable
import java.util.*
import java.util.function.Function

@Component
class JwtTokenUtil : Serializable {

    private val clock = DefaultClock.INSTANCE

    @Value("\${jwt.secret}")
    private val secret: String? = null

    @Value("\${jwt.expiration}")
    private val expiration: Long? = null

    fun getUsernameFromToken(token: String): String {
        return getClaimFromToken(token, Function { it.subject })
    }

    fun getIssuedAtDateFromToken(token: String): Date {
        return getClaimFromToken(token, Function { it.issuedAt })
    }

    fun getExpirationDateFromToken(token: String): Date {
        return getClaimFromToken(token, Function { it.expiration })
    }

    fun getAudienceFromToken(token: String): String {
        return getClaimFromToken(token, Function { it.audience })
    }

    fun <T> getClaimFromToken(token: String, claimsResolver: Function<Claims, T>): T {
        val claims = getAllClaimsFromToken(token)
        return claimsResolver.apply(claims)
    }

    private fun getAllClaimsFromToken(token: String): Claims {
        return Jwts.parser()
                .setSigningKey(secret)
                .parseClaimsJws(token)
                .getBody()
    }

    private fun isTokenExpired(token: String): Boolean {
        val expiration = getExpirationDateFromToken(token)
        return expiration.before(clock.now())
    }

    private fun isCreatedBeforeLastPasswordReset(created: Date, lastPasswordReset: Date?): Boolean {
        return lastPasswordReset != null && created.before(lastPasswordReset)
    }

    private fun generateAudience(device: Device): String {
        var audience = AUDIENCE_UNKNOWN
        if (device.isNormal()) {
            audience = AUDIENCE_WEB
        } else if (device.isTablet()) {
            audience = AUDIENCE_TABLET
        } else if (device.isMobile()) {
            audience = AUDIENCE_MOBILE
        }
        return audience
    }

    private fun ignoreTokenExpiration(token: String): Boolean {
        val audience = getAudienceFromToken(token)
        return AUDIENCE_TABLET == audience || AUDIENCE_MOBILE == audience
    }

    fun generateToken(userDetails: UserDetails, device: Device): String {
        val claims = HashMap<String, Any>()
        return doGenerateToken(claims, userDetails.username, generateAudience(device))
    }

    private fun doGenerateToken(claims: Map<String, Any>, subject: String, audience: String): String {
        val createdDate = clock.now()
        val expirationDate = calculateExpirationDate(createdDate)

        println("doGenerateToken " + createdDate)

        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setAudience(audience)
                .setIssuedAt(createdDate)
                .setExpiration(expirationDate)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun canTokenBeRefreshed(token: String, lastPasswordReset: Date): Boolean? {
        val created = getIssuedAtDateFromToken(token)
        return !isCreatedBeforeLastPasswordReset(created, lastPasswordReset) && (!isTokenExpired(token) || ignoreTokenExpiration(token))
    }

    fun refreshToken(token: String): String {
        val createdDate = clock.now()
        val expirationDate = calculateExpirationDate(createdDate)

        val claims = getAllClaimsFromToken(token)
        claims.setIssuedAt(createdDate)
        claims.setExpiration(expirationDate)

        return Jwts.builder()
                .setClaims(claims)
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact()
    }

    fun validateToken(token: String, userDetails: UserDetails): Boolean? {
        val user = userDetails as JwtUser
        val username = getUsernameFromToken(token)
        val created = getIssuedAtDateFromToken(token)
        //final Date expiration = getExpirationDateFromToken(token);
        return (username == user.username
                && !isTokenExpired(token)
                && !isCreatedBeforeLastPasswordReset(created, user.lastPasswordResetDate))
    }

    private fun calculateExpirationDate(createdDate: Date): Date {
        return Date(createdDate.time + expiration!! * 1000)
    }

    companion object {

        private const val serialVersionUID = -3301605591108950415L

        internal val CLAIM_KEY_USERNAME = "sub"
        internal val CLAIM_KEY_AUDIENCE = "aud"
        internal val CLAIM_KEY_CREATED = "iat"

        internal val AUDIENCE_UNKNOWN = "unknown"
        internal val AUDIENCE_WEB = "web"
        internal val AUDIENCE_MOBILE = "mobile"
        internal val AUDIENCE_TABLET = "tablet"
    }
}

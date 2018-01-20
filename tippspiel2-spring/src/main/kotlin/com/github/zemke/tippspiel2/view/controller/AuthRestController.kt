package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.security.JwtAuthenticationRequest
import com.github.zemke.tippspiel2.security.JwtTokenUtil
import com.github.zemke.tippspiel2.security.JwtUser
import com.github.zemke.tippspiel2.service.JwtAuthenticationResponse
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.http.ResponseEntity
import org.springframework.mobile.device.Device
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.*
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/auth")
class AuthRestController(@Autowired private val jwtTokenUtil: JwtTokenUtil,
                         @Autowired private val authenticationManager: AuthenticationManager,
                         @Autowired private val userDetailsService: UserDetailsService,
                         @Value("\${jwt.header}") private val tokenHeader: String) {

    @GetMapping("")
    fun getAuthenticatedUser(request: HttpServletRequest): JwtUser {
        val token = request.getHeader(tokenHeader).substring(7)
        val username = jwtTokenUtil.getUsernameFromToken(token)
        return userDetailsService.loadUserByUsername(username) as JwtUser
    }

    @PostMapping("")
    @Throws(AuthenticationException::class)
    fun createAuthenticationToken(@RequestBody authenticationRequest: JwtAuthenticationRequest, device: Device): ResponseEntity<*> {

        // Perform the security
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        authenticationRequest.username,
                        authenticationRequest.password
                )
        )
        SecurityContextHolder.getContext().authentication = authentication

        // Reload password post-security so we can generate token
        val userDetails = userDetailsService.loadUserByUsername(authenticationRequest.username)
        val token = jwtTokenUtil.generateToken(userDetails, device)

        // Return the token
        return ResponseEntity.ok<Any>(JwtAuthenticationResponse(token))
    }

    @GetMapping("/refresh")
    fun refreshAndGetAuthenticationToken(request: HttpServletRequest): ResponseEntity<*> {
        val authToken = request.getHeader(tokenHeader)
        val token = authToken.substring(7)
        val username = jwtTokenUtil.getUsernameFromToken(token)
        val user = userDetailsService.loadUserByUsername(username) as JwtUser

        return if (jwtTokenUtil.canTokenBeRefreshed(token, user.lastPasswordResetDate!!)!!) {
            val refreshedToken = jwtTokenUtil.refreshToken(token)
            ResponseEntity.ok<Any>(JwtAuthenticationResponse(refreshedToken))
        } else {
            ResponseEntity.badRequest().body<Any>(null)
        }
    }
}

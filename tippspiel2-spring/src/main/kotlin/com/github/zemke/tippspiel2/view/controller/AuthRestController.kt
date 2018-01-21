package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.core.authentication.AuthenticatedUser
import com.github.zemke.tippspiel2.service.JsonWebTokenService
import com.github.zemke.tippspiel2.view.model.AuthenticationRequestDto
import com.github.zemke.tippspiel2.view.model.JsonWebTokenDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.mobile.device.Device
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/auth")
class AuthRestController(@Autowired private val jsonWebTokenService: JsonWebTokenService,
                         @Autowired private val authenticationManager: AuthenticationManager,
                         @Autowired private val userDetailsService: UserDetailsService) {

    @GetMapping("")
    fun getAuthenticatedUser(request: HttpServletRequest): AuthenticatedUser {
        val username = jsonWebTokenService.getUsernameFromToken(jsonWebTokenService.extractToken(request))
        return (userDetailsService.loadUserByUsername(username) as AuthenticatedUser)
    }

    @PostMapping("")
    @Throws(AuthenticationException::class)
    fun createAuthenticationToken(
            @RequestBody authenticationRequestDto: AuthenticationRequestDto, device: Device): ResponseEntity<JsonWebTokenDto> {
        val authentication = authenticationManager.authenticate(
                UsernamePasswordAuthenticationToken(
                        authenticationRequestDto.username,
                        authenticationRequestDto.password
                )
        )

        SecurityContextHolder.getContext().authentication = authentication

        val userDetails = userDetailsService.loadUserByUsername(authenticationRequestDto.username)
        val token = jsonWebTokenService.generateToken(userDetails, device)

        return ResponseEntity.ok(JsonWebTokenDto(token))
    }

    @GetMapping("/refresh")
    fun refreshAndGetAuthenticationToken(request: HttpServletRequest): ResponseEntity<JsonWebTokenDto?> {
        val token = jsonWebTokenService.extractToken(request)
        val user = userDetailsService.loadUserByUsername(jsonWebTokenService.getUsernameFromToken(token)) as AuthenticatedUser

        return when {
            jsonWebTokenService.canTokenBeRefreshed(token, user.lastPasswordResetDate!!) ->
                ResponseEntity.ok(JsonWebTokenDto(jsonWebTokenService.refreshToken(token)))
            else ->
                ResponseEntity.badRequest().body(null)
        }
    }
}

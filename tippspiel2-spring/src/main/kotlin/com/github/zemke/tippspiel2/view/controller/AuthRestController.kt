package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.core.authentication.AuthenticatedUser
import com.github.zemke.tippspiel2.service.JsonWebTokenService
import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.view.exception.UnauthorizedException
import com.github.zemke.tippspiel2.view.model.AuthenticationRequestDto
import com.github.zemke.tippspiel2.view.model.JsonWebTokenDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.AuthenticationException
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import javax.servlet.http.HttpServletRequest

@RestController
@RequestMapping("/api/auth")
class AuthRestController(@Autowired private val jsonWebTokenService: JsonWebTokenService,
                         @Autowired private val userService: UserService,
                         @Autowired private val userDetailsService: UserDetailsService) {

    @GetMapping("")
    fun getAuthenticatedUser(request: HttpServletRequest): AuthenticatedUser {
        val email = jsonWebTokenService.getSubjectFromToken(jsonWebTokenService.assertToken(request) ?: throw UnauthorizedException())
        return (userDetailsService.loadUserByUsername(email) as AuthenticatedUser)
    }

    @PostMapping("")
    @Throws(AuthenticationException::class)
    fun createAuthenticationToken(
            @RequestBody authenticationRequestDto: AuthenticationRequestDto): ResponseEntity<JsonWebTokenDto> {
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(JsonWebTokenDto(userService.authenticate(
                        authenticationRequestDto.email, authenticationRequestDto.password)))
    }

    @GetMapping("/{token}")
    fun refreshAndGetAuthenticationToken(@PathVariable("token") token: String): ResponseEntity<JsonWebTokenDto?> {
        val user = userDetailsService.loadUserByUsername(jsonWebTokenService.getSubjectFromToken(token)) as AuthenticatedUser

        return when {
            jsonWebTokenService.canTokenBeRefreshed(token, user.lastPasswordResetDate) ->
                ResponseEntity.ok(JsonWebTokenDto(jsonWebTokenService.refreshToken(token)))
            else ->
                ResponseEntity.badRequest().body(null)
        }
    }
}

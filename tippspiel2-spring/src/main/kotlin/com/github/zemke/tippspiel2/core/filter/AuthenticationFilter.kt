package com.github.zemke.tippspiel2.core.filter

import com.github.zemke.tippspiel2.service.JsonWebTokenService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import javax.servlet.FilterChain
import javax.servlet.ServletException
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter : OncePerRequestFilter() {

    @Autowired
    private lateinit var jsonWebTokenService: JsonWebTokenService;

    @Autowired
    private lateinit var userDetailsService: UserDetailsService;

    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain) {
        try {
            val token = jsonWebTokenService.extractToken(request)
            val username: String = jsonWebTokenService.getUsernameFromToken(token)

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = this.userDetailsService.loadUserByUsername(username) // TODO Store data in token to avoid extra DB call.

                if (jsonWebTokenService.validateToken(token, userDetails)) {
                    val authentication = UsernamePasswordAuthenticationToken(userDetails, null, userDetails.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        } finally {
            return chain.doFilter(request, response)
        }
    }
}

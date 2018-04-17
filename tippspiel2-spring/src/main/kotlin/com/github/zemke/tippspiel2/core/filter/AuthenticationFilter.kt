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
            val authToken = jsonWebTokenService.assertToken(request)
            val username = jsonWebTokenService.getSubjectFromToken(authToken)

            if (SecurityContextHolder.getContext().authentication == null) {
                val userDetails = this.userDetailsService.loadUserByUsername(username)

                if (jsonWebTokenService.validateToken(authToken, userDetails)) {
                    val authentication = UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.authorities)
                    authentication.details = WebAuthenticationDetailsSource().buildDetails(request)
                    SecurityContextHolder.getContext().authentication = authentication
                }
            }
        } catch (ignored: Exception) {
        }

        chain.doFilter(request, response)
    }
}

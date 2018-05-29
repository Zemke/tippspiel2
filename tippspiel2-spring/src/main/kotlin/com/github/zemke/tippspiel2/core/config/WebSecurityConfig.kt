package com.github.zemke.tippspiel2.core.config

import com.github.zemke.tippspiel2.core.authentication.Http401JwtAuthenticationEntryPoint
import com.github.zemke.tippspiel2.core.filter.AuthenticationFilter
import com.github.zemke.tippspiel2.persistence.model.enumeration.UserRole
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import org.springframework.web.cors.CorsConfiguration

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
class WebSecurityConfig(
        @Autowired private val http401JwtAuthenticationEntryPoint: Http401JwtAuthenticationEntryPoint,
        @Autowired private val userDetailsService: UserDetailsService
) : WebSecurityConfigurerAdapter() {

    @Autowired
    @Throws(Exception::class)
    fun configureAuthentication(authenticationManagerBuilder: AuthenticationManagerBuilder) {
        authenticationManagerBuilder
                .userDetailsService<UserDetailsService>(this.userDetailsService)
                .passwordEncoder(passwordEncoder())
    }

    @Bean
    override fun authenticationManagerBean(): AuthenticationManager {
        return super.authenticationManagerBean()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }

    @Bean
    @Throws(Exception::class)
    fun authenticationTokenFilterBean(): AuthenticationFilter {
        return AuthenticationFilter()
    }

    @Throws(Exception::class)
    override fun configure(httpSecurity: HttpSecurity) {
        httpSecurity
                .csrf().disable()
                .cors().configurationSource {
                    with(CorsConfiguration()) {
                        applyPermitDefaultValues()
                        addAllowedMethod(HttpMethod.PUT)
                        this
                    }
                }.and()
                .exceptionHandling().authenticationEntryPoint(http401JwtAuthenticationEntryPoint).and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and()
                .authorizeRequests()
                .antMatchers(HttpMethod.OPTIONS, "/**").permitAll()
                .antMatchers(
                        HttpMethod.GET,
                        "/",
                        "/img/**/*",
                        "/fonts/**/*",
                        "/**/*.html",
                        "/**/*.css",
                        "/**/*.js",
                        "/**/*.js.map").permitAll()
                .antMatchers("/api/hellos/**").hasRole(UserRole.ROLE_ADMIN.unPrefixed())
                .antMatchers(HttpMethod.POST, "/api/auth").permitAll()
                .antMatchers(HttpMethod.POST, "/api/betting-games").hasRole(UserRole.ROLE_ADMIN.unPrefixed())
                .antMatchers(HttpMethod.GET, "/api/betting-games").permitAll()
                .antMatchers(HttpMethod.POST, "/api/competitions").hasRole(UserRole.ROLE_ADMIN.unPrefixed())
                .antMatchers(HttpMethod.PUT, "/api/competitions").hasRole(UserRole.ROLE_ADMIN.unPrefixed())
                .antMatchers(HttpMethod.POST, "/api/users").permitAll()
                .antMatchers(HttpMethod.GET, "/api/teams").permitAll()
                .anyRequest().authenticated()

        httpSecurity
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter::class.java)

        httpSecurity
                .headers()
                .frameOptions().sameOrigin()
                .cacheControl()
    }
}

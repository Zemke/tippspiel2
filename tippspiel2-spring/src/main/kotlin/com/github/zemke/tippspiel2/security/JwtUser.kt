package com.github.zemke.tippspiel2.security

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.util.*

class JwtUser(
        val id: Long, val firstName: String, val lastName: String,
        val email: String, val plainPassword: String, val lastPasswordResetDate: Date?) : UserDetails {

    override fun getAuthorities(): Collection<GrantedAuthority> {
        return listOf<GrantedAuthority>(SimpleGrantedAuthority("USER"))
    }

    override fun isEnabled(): Boolean = true

    override fun getUsername(): String = email

    override fun isCredentialsNonExpired(): Boolean = true

    override fun getPassword(): String = plainPassword

    override fun isAccountNonExpired(): Boolean = true

    override fun isAccountNonLocked(): Boolean = true
}

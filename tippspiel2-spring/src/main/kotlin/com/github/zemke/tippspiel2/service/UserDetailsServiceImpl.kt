package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.repository.UserRepository
import com.github.zemke.tippspiel2.core.authentication.AuthenticatedUser
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
class UserDetailsServiceImpl(@Autowired private val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): UserDetails {
        val user = userRepository.findByEmail(username)

        return when (user) {
            null -> throw UsernameNotFoundException("No user found with username $username.")
            else -> AuthenticatedUser.create(user)
        }
    }
}

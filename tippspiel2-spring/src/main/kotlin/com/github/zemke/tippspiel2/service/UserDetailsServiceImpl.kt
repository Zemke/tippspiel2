package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.authentication.AuthenticatedUser
import com.github.zemke.tippspiel2.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Primary
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service

@Service
@Primary
class  UserDetailsServiceImpl(@Autowired private val userRepository: UserRepository) : UserDetailsService {

    @Throws(UsernameNotFoundException::class)
    override fun loadUserByUsername(username: String): AuthenticatedUser {
        val user = userRepository.findByEmail(username)

        return when (user) {
            null -> throw UsernameNotFoundException("No user found with email $username.")
            else -> AuthenticatedUser.create(user)
        }
    }
}

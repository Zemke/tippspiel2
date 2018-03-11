package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.core.properties.AuthenticationProperties
import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.persistence.model.embeddable.FullName
import com.github.zemke.tippspiel2.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class UserService(
        @Autowired private var userRepository: UserRepository,
        @Autowired private val authenticationProperties: AuthenticationProperties
) {


    fun addUser(firstName: String, lastName: String, email: String, plainPassword: String): User {
        return userRepository.save(User(
                FullName(firstName, lastName), email,
                BCrypt.hashpw(plainPassword, authenticationProperties.bcryptSalt)))
    }

    fun getUser(id: Long): User? {
        return userRepository.getOne(id)
    }

    fun findUsers(users: List<Long>): List<User> {
        return userRepository.findAllById(users)
    }

    fun findUserByEmail(email: String): User? = userRepository.findByEmail(email)
}

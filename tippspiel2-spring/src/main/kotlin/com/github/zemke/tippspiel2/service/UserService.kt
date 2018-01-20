package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.entity.User
import com.github.zemke.tippspiel2.entity.embeddable.FullName
import com.github.zemke.tippspiel2.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Service

@Service
class UserService(
        @Autowired private var userRepository: UserRepository) {


    @Value("\${password.bcrypt-salt}")
    private val bCryptSalt: String? = null

    fun addUser(firstName: String, lastName: String, email: String, plainPassword: String): User {
        return userRepository.save(User(FullName(firstName, lastName), email, BCrypt.hashpw(plainPassword, bCryptSalt)))
    }

    fun getUser(id: Long): User? {
        return userRepository.findOne(id)
    }
}

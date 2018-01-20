package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.entity.User
import com.github.zemke.tippspiel2.entity.embeddable.FullName
import com.github.zemke.tippspiel2.persistence.repository.UserRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class UserService(
        @Autowired private var userRepository: UserRepository) {

    fun addUser(firstName: String, lastName: String, email: String, password: String): User {
        return userRepository.save(User(FullName(firstName, lastName), email, password))
    }
}

package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.entity.User
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface UserRepository : JpaRepository<User, Long> {

    fun findByEmail(email: String): User
}

package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Role
import com.github.zemke.tippspiel2.persistence.model.enumeration.UserRole
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface RoleRepository : JpaRepository<Role, Long> {

    fun findByNameIn(name: List<UserRole>): List<Role>
}

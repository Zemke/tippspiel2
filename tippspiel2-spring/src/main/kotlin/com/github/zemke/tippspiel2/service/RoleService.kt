package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Role
import com.github.zemke.tippspiel2.persistence.model.enumeration.UserRole
import com.github.zemke.tippspiel2.persistence.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class RoleService(
    @Autowired private var roleRepository: RoleRepository
) {

    fun initRoles(): List<Role> = roleRepository.saveAll(UserRole.values().map { Role(it) })
}

package com.github.zemke.tippspiel2.core.authentication

import com.github.zemke.tippspiel2.persistence.model.Role
import com.github.zemke.tippspiel2.persistence.model.enumeration.UserRole
import com.github.zemke.tippspiel2.persistence.repository.RoleRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Component
open class RoleInitializer : ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private lateinit var roleRepository: RoleRepository

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        roleRepository.saveAll(UserRole.values().map { Role(it) })
    }
}

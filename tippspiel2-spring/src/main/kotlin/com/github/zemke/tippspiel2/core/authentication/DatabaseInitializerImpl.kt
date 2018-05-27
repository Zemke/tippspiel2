package com.github.zemke.tippspiel2.core.authentication

import com.github.zemke.tippspiel2.core.profile.Prod
import com.github.zemke.tippspiel2.core.properties.AuthenticationProperties
import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.persistence.model.embeddable.FullName
import com.github.zemke.tippspiel2.persistence.repository.UserRepository
import com.github.zemke.tippspiel2.service.RoleService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.security.crypto.bcrypt.BCrypt
import org.springframework.stereotype.Component

@Prod
@Component
class DatabaseInitializerImpl : DatabaseInitializer {

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var userRepository: UserRepository

    @Autowired
    private lateinit var authenticationProperties: AuthenticationProperties

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        userRepository.save(User(
                fullName = FullName("Florian", "Zemke"),
                password = BCrypt.hashpw("admin", authenticationProperties.bcryptSalt),
                email = "flzemke@gmail.com",
                roles = roleService.initRoles()
        ))
    }
}

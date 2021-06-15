package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.persistence.model.enumeration.UserRole
import com.github.zemke.tippspiel2.test.util.IntegrationTest
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@IntegrationTest
class HelloRestControllerTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun testRoleRestrictions() {
        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/hellos")
                .with(user("admin").roles(UserRole.ROLE_ADMIN.unPrefixed()))
        )
            .andExpect(status().isOk)

        this.mockMvc.perform(
            MockMvcRequestBuilders
                .get("/api/hellos")
                .with(user("user").roles(UserRole.ROLE_USER.unPrefixed()))
        )
            .andExpect(status().isForbidden)
    }
}

package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.SpringBootTippspiel2Application
import com.github.zemke.tippspiel2.core.config.WebSecurityConfig
import com.github.zemke.tippspiel2.persistence.model.enumeration.UserRole
import com.github.zemke.tippspiel2.test.util.IntegrationTest
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.context.junit4.SpringRunner
import org.springframework.test.context.web.WebAppConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status
import org.springframework.test.web.servlet.setup.DefaultMockMvcBuilder
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext


@RunWith(SpringRunner::class)
@WebAppConfiguration
@ContextConfiguration(classes = [WebSecurityConfig::class])
@IntegrationTest
class HelloRestControllerTest {

    @Autowired
    private lateinit var webApplicationContext: WebApplicationContext

    private lateinit var mockMvc: MockMvc

    @Before
    fun setUp() {
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply<DefaultMockMvcBuilder>(springSecurity())
                .build()
    }

    @Test
    fun testRoleRestrictions() {
        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/hellos")
                .with(user("admin").roles(UserRole.ROLE_ADMIN.unPrefixed())))
                .andExpect(status().isOk)

        this.mockMvc.perform(MockMvcRequestBuilders
                .get("/api/hellos")
                .with(user("user").roles(UserRole.ROLE_USER.unPrefixed())))
                .andExpect(status().isForbidden)
    }

}

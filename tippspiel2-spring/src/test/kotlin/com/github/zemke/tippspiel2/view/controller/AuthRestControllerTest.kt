package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.core.authentication.AuthenticatedUser
import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.repository.BettingGameRepository
import com.github.zemke.tippspiel2.persistence.repository.CompetitionRepository
import com.github.zemke.tippspiel2.service.JsonWebTokenService
import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.test.util.IntegrationTest
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import com.github.zemke.tippspiel2.view.model.AuthenticationRequestDto
import com.github.zemke.tippspiel2.view.model.JsonWebTokenDto
import com.github.zemke.tippspiel2.view.util.JacksonUtils
import io.jsonwebtoken.lang.Assert
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@IntegrationTest
class AuthRestControllerTest {

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var bettingGameRepository: BettingGameRepository

    @Autowired
    private lateinit var competitionRepository: CompetitionRepository

    @Autowired
    private lateinit var jsonWebTokenService: JsonWebTokenService

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun createAuthenticationToken() {
        val plainPassword = "hamburger"
        val email = "Florian.Zemke@btc-ag.com"
        val requestPayload = JacksonUtils.toJson(AuthenticationRequestDto(email, plainPassword))
        val user = userService.addUser("Florian", "Zemke", email, plainPassword, createBettingGame())

        this.mockMvc.perform(
            post("/api/auth")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestPayload)
        )
            .andExpect(status().isCreated)
            .andDo {
                val token = JacksonUtils.fromJson<JsonWebTokenDto>(it.response.contentAsString).token
                Assert.isTrue(jsonWebTokenService.validateToken(token, AuthenticatedUser.create(user)))
                Assert.isTrue(jsonWebTokenService.getSubjectFromToken(token) == user.email)
            }
    }

    private fun createBettingGame(): BettingGame {
        val bettingGame = PersistenceUtils.instantiateBettingGame()
        return bettingGameRepository.save(
            bettingGame.copy(competition = competitionRepository.save(bettingGame.competition))
        )
    }
}

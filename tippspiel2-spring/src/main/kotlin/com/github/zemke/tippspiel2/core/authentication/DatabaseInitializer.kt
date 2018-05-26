package com.github.zemke.tippspiel2.core.authentication

import com.github.zemke.tippspiel2.core.profile.Dev
import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.persistence.model.BettingGame
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import com.github.zemke.tippspiel2.persistence.model.enumeration.UserRole
import com.github.zemke.tippspiel2.service.BetService
import com.github.zemke.tippspiel2.service.BettingGameService
import com.github.zemke.tippspiel2.service.FixtureService
import com.github.zemke.tippspiel2.service.FootballDataService
import com.github.zemke.tippspiel2.service.NULL_TEAM_ID
import com.github.zemke.tippspiel2.service.RoleService
import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component

@Dev
@Component
class DatabaseInitializer : ApplicationListener<ContextRefreshedEvent> {

    @Autowired
    private lateinit var roleService: RoleService

    @Autowired
    private lateinit var userService: UserService

    @Autowired
    private lateinit var footballDataService: FootballDataService

    @Autowired
    private lateinit var fixtureService: FixtureService

    @Autowired
    private lateinit var bettingGameService: BettingGameService

    @Autowired
    private lateinit var betService: BetService

    private val COMPETITION_ID = 467L

    override fun onApplicationEvent(event: ContextRefreshedEvent?) {
        val userRole = listOf(UserRole.ROLE_USER)
        roleService.initRoles()

        val competitionDto = footballDataService.requestCompetition(COMPETITION_ID)
        val fixtureWrappedListDto = footballDataService.requestFixtures(COMPETITION_ID)
        val teamWrappedListDto = footballDataService.requestTeams(COMPETITION_ID)

        val competition = FootballDataCompetitionDto.fromDto(competitionDto)
        val teams = teamWrappedListDto.teams.map { FootballDataTeamDto.fromDto(it, competition) }
        val fixtures = fixtureWrappedListDto.fixtures
                .filter { FixtureStatus.OF_INTEREST.contains(it.status) }
                .filter { it.homeTeamId != NULL_TEAM_ID && it.awayTeamId != NULL_TEAM_ID }
                .map { FootballDataFixtureDto.fromDto(it, teams, competition) }

        fixtureService.saveMany(fixtures)

        val bettingGame = bettingGameService.saveBettingGame(BettingGame(name = "Schokolade", competition = competition))

        val florianZemke = userService.addUser("Florian", "Zemke", "fz@fz.fz", "fz", bettingGame, UserRole.values().asList())
        userService.addUser("Sönke", "Martens", "sm@sm.sm", "sm", bettingGame, userRole)
        userService.addUser("Carsten", "Krüwel", "ck@ck.ck", "ck", bettingGame, userRole)
        userService.addUser("Torsten", "Abels", "ta@ta.ta", "ta", bettingGame, userRole)

        betService.save(Bet(
                id = null,
                bettingGame = bettingGame,
                fixture = fixtures[0],
                goalsAwayTeamBet = 3,
                goalsHomeTeamBet = 1,
                user = florianZemke))
    }
}

package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto
import com.github.zemke.tippspiel2.view.model.FootballDataFixtureWrappedListDto
import com.github.zemke.tippspiel2.view.model.FootballDataTeamWrappedListDto

/**
 * For some reason football-data.org chose to assign fixtures with not yet determined opponents
 * not as teams being actually `null`, but rather a team with ID 757 that resembles a team with all
 * its attrbibutes `null`.
 */
const val NULL_TEAM_ID: Long = 757

interface FootballDataService {

    fun requestCompetition(competitionId: Long): FootballDataCompetitionDto
    fun requestFixtures(competitionId: Long): FootballDataFixtureWrappedListDto
    fun requestTeams(competitionId: Long): FootballDataTeamWrappedListDto
}

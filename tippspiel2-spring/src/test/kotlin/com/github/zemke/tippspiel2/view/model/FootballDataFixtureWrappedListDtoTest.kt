package com.github.zemke.tippspiel2.view.model

import org.junit.Assert
import org.junit.jupiter.api.Test

class FootballDataFixtureWrappedListDtoTest {

    /**
     * <pre>
     * "duration" : "PENALTY_SHOOTOUT",
     * "extraTime" : {
     *    "awayTeam" : 0,
     *    "homeTeam" : 0
     * },
     * "fullTime" : {
     *    "awayTeam" : 8,
     *    "homeTeam" : 7
     * },
     * "halfTime" : {
     *    "awayTeam" : 1,
     *    "homeTeam" : 0
     * },
     * "penalties" : {
     *    "awayTeam" : 5,
     *    "homeTeam" : 4
     * },
     * "winner" : "AWAY_TEAM"
     */
    @Test
    fun regularTimeScore() {
        val result = FootballDataFixtureResultDto(
            extraTime = FootballDataFixtureScoreDto(homeTeam = 0, awayTeam = 0),
            fullTime = FootballDataFixtureScoreDto(homeTeam = 8, awayTeam = 7),
            halfTime = FootballDataFixtureScoreDto(homeTeam = 1, awayTeam = 0),
            penalties = FootballDataFixtureScoreDto(homeTeam = 5, awayTeam = 4),
        )
        Assert.assertEquals(result.regularTime, FootballDataFixtureScoreDto(homeTeam = 3, awayTeam = 3))
        Assert.assertEquals(result.regularTime.homeTeam, 3)
        Assert.assertEquals(result.regularTime.awayTeam, 3)
    }
}

package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Bet
import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import org.intellij.lang.annotations.Language
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import org.springframework.stereotype.Repository

@Repository
interface BetRepository : JpaRepository<Bet, Long> {

    @Language("JPAQL")
    @Query("select b from Bet b where b.fixture.competition = :competition and b.fixture.status = :fixtureStatus")
    fun findByCompetitionAndFixtureStatus(
            @Param("competition") competition: Competition,
            @Param("fixtureStatus") fixtureStatus: FixtureStatus): List<Bet>
}

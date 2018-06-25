package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.model.enumeration.FixtureStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FixtureRepository : JpaRepository<Fixture, Long> {

    fun findFixturesByCompetitionAndManualFalse(competition: Competition): List<Fixture>
    fun findFixturesByCompetition(competition: Competition): List<Fixture>
    fun findByStatusIn(status: List<FixtureStatus>): List<Fixture>
}

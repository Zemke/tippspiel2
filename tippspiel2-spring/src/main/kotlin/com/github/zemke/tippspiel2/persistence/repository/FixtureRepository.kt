package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface FixtureRepository : JpaRepository<Fixture, Long> {

    fun findFixturesByCompetitionAndManualFalse(competition: Competition): List<Fixture>
}

package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.repository.FixtureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class FixtureService(
    @Autowired private var fixtureRepository: FixtureRepository
) {

    fun saveMany(fixtures: List<Fixture>): List<Fixture> = fixtureRepository.saveAll(fixtures)

    fun getById(fixtureId: Long): Optional<Fixture> = fixtureRepository.findById(fixtureId)

    fun findFixturesByCompetitionAndManualFalse(competition: Competition): List<Fixture> =
        fixtureRepository.findFixturesByCompetitionAndManualFalse(competition)

    fun findFixturesByCompetition(competition: Competition) =
        fixtureRepository.findFixturesByCompetition(competition)

    fun findAll() =
        fixtureRepository.findAll()
}

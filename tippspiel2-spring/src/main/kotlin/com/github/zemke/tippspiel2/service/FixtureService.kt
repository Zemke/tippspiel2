package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Fixture
import com.github.zemke.tippspiel2.persistence.repository.FixtureRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class FixtureService(
        @Autowired private var fixtureRepository: FixtureRepository
) {

    fun saveMany(fixtures: List<Fixture>): List<Fixture> {
        return fixtureRepository.saveAll(fixtures)
    }

    fun getById(fixtureId: Long): Fixture? = fixtureRepository.findById(fixtureId).orElse(null)
}

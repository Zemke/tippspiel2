package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.repository.CompetitionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CompetitionService(
        @Autowired private var competitionRepository: CompetitionRepository
) {
    fun find(competitionId: Long): Competition = competitionRepository.findOne(competitionId)
}

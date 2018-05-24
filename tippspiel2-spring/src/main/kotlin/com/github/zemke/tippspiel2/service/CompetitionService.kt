package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.repository.CompetitionRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.*

@Service
class CompetitionService(
        @Autowired private var competitionRepository: CompetitionRepository
) {

    fun find(competitionId: Long): Optional<Competition> =
            competitionRepository.findById(competitionId)

    fun findByCurrentTrue(): Competition? =
            competitionRepository.findByCurrentTrue()

    fun findAll(): List<Competition> =
            competitionRepository.findAll()

    @Transactional
    fun setCurrentCompetition(newCurrentCompetition: Competition): Competition {
        val currentCurrentCompetition = findByCurrentTrue()

        if (currentCurrentCompetition != null) {
            currentCurrentCompetition.current = false
            competitionRepository.save(currentCurrentCompetition)
        }

        newCurrentCompetition.current = true
        return competitionRepository.save(newCurrentCompetition)
    }
}

package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Competition
import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.repository.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service
import java.util.*

@Service
class TeamService(
    @Autowired private var teamRepository: TeamRepository
) {

    fun find(teamId: Long): Optional<Team> =
        teamRepository.findById(teamId)

    fun createTeam(team: Team): Team =
        teamRepository.save(team)

    fun findByCompetition(competition: Competition) =
        teamRepository.findByCompetition(competition)

    fun findAll(): List<Team> =
        teamRepository.findAll()
}

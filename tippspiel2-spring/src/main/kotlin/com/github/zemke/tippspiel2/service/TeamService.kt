package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Team
import com.github.zemke.tippspiel2.persistence.repository.TeamRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
open class TeamService(
        @Autowired private var teamRepository: TeamRepository
) {

    fun find(teamId: Long): Team = teamRepository.findOne(teamId)

    fun createTeam(team: Team): Team = teamRepository.save(team)
}

package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.view.model.FootballDataCompetitionDto

interface FootballDataService {

    fun requestCompetition(competitionId: Long): FootballDataCompetitionDto
}

package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class CompetitionCreationDto(val id: Long, val current: Boolean?)

package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.view.util.DataTransferObject

/**
 * The ID the only value that's required since it's then used to retrieve the other data from the external API.
 */
@DataTransferObject
data class CompetitionCreationDto(val id: Long)

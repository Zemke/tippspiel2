package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class UserCreationDto(

        val firstName: String,
        val lastName: String,
        val email: String,
        val password: String,
        val bettingGame: Long
)

package com.github.zemke.tippspiel2.view.model

import java.util.*

data class RestError(
        val message: String,
        val path: String,
        val status: Int,
        val timestamp: Date,
        val locKey: String
)








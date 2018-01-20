package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty


data class AuthenticationRequestDto(

        @JsonProperty("firstName") val firstName: String,
        @JsonProperty("lastName") val lastName: String,
        @JsonProperty("email") val email: String,
        @JsonProperty("password") val password: String
)

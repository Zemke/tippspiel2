package com.github.zemke.tippspiel2.view.model

import com.fasterxml.jackson.annotation.JsonProperty
import com.github.zemke.tippspiel2.entity.User


data class UserDto(

        @JsonProperty("id") val id: Long,
        @JsonProperty("firstName") val firstName: String,
        @JsonProperty("lastName") val lastName: String,
        @JsonProperty("email") val email: String
) {
    companion object {

        fun toDto(user: User): UserDto =
                UserDto(user.id!!, user.fullName.firstName, user.fullName.lastName, user.email)
    }
}

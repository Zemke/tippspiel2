package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.entity.User


data class UserDto(

        val id: Long,
        val firstName: String,
        val lastName: String,
        val email: String
) {
    companion object {

        fun toDto(user: User): UserDto =
                UserDto(user.id!!, user.fullName.firstName, user.fullName.lastName, user.email)
    }
}

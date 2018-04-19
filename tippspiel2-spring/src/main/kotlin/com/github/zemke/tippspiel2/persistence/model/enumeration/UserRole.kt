package com.github.zemke.tippspiel2.persistence.model.enumeration

enum class UserRole {
    ROLE_USER,
    ROLE_ADMIN;

    fun unPrefixed(): String = this.name.substring(5)
}

package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Community
import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.view.util.DataTransferObject

@DataTransferObject
data class CommunityCreationDto(

        val name: String,
        val users: List<Long>
) {

    companion object {

        fun fromDto(dto: CommunityCreationDto, users: List<User>): Community = Community(
                id = null,
                name = dto.name,
                users = users,
                created = null,
                modified = null
        )
    }
}

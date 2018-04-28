package com.github.zemke.tippspiel2.view.model

import com.github.zemke.tippspiel2.persistence.model.Community
import com.github.zemke.tippspiel2.view.util.DataTransferObject
import java.util.*

@DataTransferObject
data class CommunityDto(

        val id: Long,
        val name: String,
        val created: Date,
        val modified: Date,
        val users: List<UserDto>
) {

    companion object {

        fun toDto(community: Community): CommunityDto = CommunityDto(
                id = community.id!!,
                name = community.name,
                created = Date(community.created.time),
                modified = Date(community.modified.time),
                users = community.users.map { UserDto.toDto(it) }
        )
    }
}

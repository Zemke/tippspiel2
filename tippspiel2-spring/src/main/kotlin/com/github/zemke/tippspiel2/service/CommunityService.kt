package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Community
import com.github.zemke.tippspiel2.persistence.repository.CommunityRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Service

@Service
class CommunityService(
        @Autowired private var communityRepository: CommunityRepository
) {

    fun save(community: Community): Community = communityRepository.save(community)
    fun find(communityId: Long): Community = communityRepository.getOne(communityId)
}

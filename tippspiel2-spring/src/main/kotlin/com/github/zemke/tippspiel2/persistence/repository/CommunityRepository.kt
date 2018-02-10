package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Community
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface CommunityRepository : JpaRepository<Community, Long>

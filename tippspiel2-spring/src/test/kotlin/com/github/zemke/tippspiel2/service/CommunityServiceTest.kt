package com.github.zemke.tippspiel2.service

import com.github.zemke.tippspiel2.persistence.model.Community
import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.test.util.EmbeddedPostgresDataJpaTest
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.context.annotation.Import
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@EmbeddedPostgresDataJpaTest
@Import(CommunityService::class)
open class CommunityServiceTest {

    @Autowired
    private lateinit var communityService: CommunityService

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSave() {
        val community = Community(
                id = null,
                name = "World Cup",
                users = listOf(testEntityManager.find(User::class.java, 1L)),
                created = null,
                modified = null
        )

        communityService.save(community)

        Assert.assertEquals(
                community,
                testEntityManager.entityManager.createQuery("select c from Community c", Community::class.java).singleResult)
    }
}

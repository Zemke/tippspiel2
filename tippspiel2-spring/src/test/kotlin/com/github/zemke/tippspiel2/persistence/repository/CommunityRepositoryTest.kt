package com.github.zemke.tippspiel2.persistence.repository

import com.github.zemke.tippspiel2.persistence.model.Community
import com.github.zemke.tippspiel2.persistence.repository.CommunityRepository
import com.github.zemke.tippspiel2.test.util.EmbeddedPostgresDataJpaTest
import com.github.zemke.tippspiel2.test.util.PersistenceUtils
import org.junit.Assert
import org.junit.Test
import org.junit.runner.RunWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager
import org.springframework.test.context.junit4.SpringRunner

@RunWith(SpringRunner::class)
@EmbeddedPostgresDataJpaTest
open class CommunityRepositoryTest {

    @Autowired
    private lateinit var communityRepository: CommunityRepository

    @Autowired
    private lateinit var testEntityManager: TestEntityManager

    @Test
    fun testSave() {
        val community = Community(
                id = null,
                name = "World Cup",
                users = listOf(PersistenceUtils.createUser(testEntityManager)),
                created = null,
                modified = null
        )

        communityRepository.save(community)

        Assert.assertEquals(
                community,
                testEntityManager.entityManager.createQuery("select c from Community c", Community::class.java).singleResult)
    }
}

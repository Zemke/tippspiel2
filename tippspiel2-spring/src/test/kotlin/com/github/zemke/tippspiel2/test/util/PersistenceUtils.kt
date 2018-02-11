package com.github.zemke.tippspiel2.test.util

import com.github.zemke.tippspiel2.persistence.model.User
import com.github.zemke.tippspiel2.persistence.model.embeddable.FullName
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager


object PersistenceUtils {

    /**
     * Create a John Doe with his attributes appended with {@code appendToAttribute} to distinguish multiple John Does. :)
     */
    fun createUser(testEntityManager: TestEntityManager, appendToAttribute: String = "1"): User =
            testEntityManager.persist(User(
                    fullName = FullName("Jon$appendToAttribute", "Doe$appendToAttribute"),
                    email = "jon@doe.com$appendToAttribute",
                    password = "jondoe$appendToAttribute"))
}

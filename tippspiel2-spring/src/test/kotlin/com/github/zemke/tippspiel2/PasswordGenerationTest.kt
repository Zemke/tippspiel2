package com.github.zemke.tippspiel2

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.Assertions.assertTrue
import org.springframework.security.crypto.bcrypt.BCrypt

/**
 * Use this test to generate passwords with a given salt.
 */
class PasswordGenerationTest {

	@Test
	fun generatePassword() {
        val salt = "\$2a\$10\$NBVm8mNMr87fbRRJGz3XJu";
        val password = "dev"
        println("salt: " + salt)
        println("password: " + password)
        val hashed = BCrypt.hashpw(password, salt)
        println("hashed: " + hashed)
        assertTrue(BCrypt.checkpw(password, hashed))
	}
}


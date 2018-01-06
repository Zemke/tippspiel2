package com.example

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication()
open class SpringBootKotlinExampleApplication

fun main(args: Array<String>) {
    SpringApplication.run(SpringBootKotlinExampleApplication::class.java, *args)
}

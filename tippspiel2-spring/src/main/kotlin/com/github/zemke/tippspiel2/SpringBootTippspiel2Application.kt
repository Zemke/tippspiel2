package com.github.zemke.tippspiel2

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.scheduling.annotation.EnableScheduling

@SpringBootApplication
@EnableScheduling
open class SpringBootTippspiel2Application

fun main(args: Array<String>) {
    SpringApplication.run(SpringBootTippspiel2Application::class.java, *args)
}

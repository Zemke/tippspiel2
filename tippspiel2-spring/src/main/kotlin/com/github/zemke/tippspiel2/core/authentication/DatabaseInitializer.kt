package com.github.zemke.tippspiel2.core.authentication

import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent


interface DatabaseInitializer : ApplicationListener<ContextRefreshedEvent>

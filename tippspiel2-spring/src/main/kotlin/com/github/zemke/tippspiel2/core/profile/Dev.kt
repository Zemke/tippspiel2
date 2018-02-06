package com.github.zemke.tippspiel2.core.profile

import org.springframework.context.annotation.Profile
import kotlin.annotation.AnnotationRetention.RUNTIME
import kotlin.annotation.AnnotationTarget.ANNOTATION_CLASS
import kotlin.annotation.AnnotationTarget.CLASS
import kotlin.annotation.AnnotationTarget.FUNCTION
import kotlin.annotation.AnnotationTarget.TYPE

@Target(TYPE, CLASS, ANNOTATION_CLASS, FUNCTION, TYPE)
@Retention(RUNTIME)
@Profile("dev", "default")
annotation class Dev

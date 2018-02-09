package com.github.zemke.tippspiel2.view.util

import org.springframework.hateoas.Link


object FootballDataUtils {
    fun extractId(it: Link): Long {
        val split = it.href.split("/")
        return split[split.size - 1].toLong()
    }
}

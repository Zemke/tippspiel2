package com.github.zemke.tippspiel2.test.util

import org.mockito.Mockito

object MockitoUtils {

    /** [https://stackoverflow.com/a/30308199/2015430](https://stackoverflow.com/a/30308199/2015430) */
    fun <T> anyObject(): T {
        Mockito.any<T>()
        return uninitialized()
    }

    @Suppress("UNCHECKED_CAST")
    private fun <T> uninitialized(): T = null as T
}

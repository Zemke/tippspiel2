package com.github.zemke.tippspiel2.test.util

import org.springframework.http.MediaType
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter
import org.springframework.mock.http.MockHttpInputMessage
import org.springframework.mock.http.MockHttpOutputMessage
import java.io.IOException


object JacksonUtils {

    @Throws(IOException::class)
    internal fun toJson(o: Any): String {
        val mockHttpOutputMessage = MockHttpOutputMessage()
        val mappingJackson2HttpMessageConverter = MappingJackson2HttpMessageConverter()
        mappingJackson2HttpMessageConverter.write(o, MediaType.APPLICATION_JSON, mockHttpOutputMessage)
        return mockHttpOutputMessage.bodyAsString
    }

    @Throws(IOException::class, IllegalArgumentException::class)
    internal inline fun <reified T : Any> fromJson(json: String): T {
        val mockHttpInputMessage = MockHttpInputMessage(json.toByteArray())
        val mappingJackson2HttpMessageConverter = MappingJackson2HttpMessageConverter()

        if (!mappingJackson2HttpMessageConverter.canRead(T::class.java, MediaType.APPLICATION_JSON)) {
            throw IllegalArgumentException("Type ${T::class.java} cannot be read.")
        }

        @Suppress("UNCHECKED_CAST")
        return mappingJackson2HttpMessageConverter.read(T::class.java, mockHttpInputMessage) as T
    }
}

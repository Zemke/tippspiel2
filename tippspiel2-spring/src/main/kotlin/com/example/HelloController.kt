package com.example

import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@RestController
class HelloController() {

    @GetMapping("/hello")
    fun helloKotlin(): String {
        return "hello world"
    }

    @GetMapping("/hello-json")
    fun helloKotlinJson(): ResponseEntity<HelloDto> {
        return ResponseEntity.ok(HelloDto("world"));
    }
}

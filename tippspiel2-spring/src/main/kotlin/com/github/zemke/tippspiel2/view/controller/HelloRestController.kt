package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.view.model.HelloDto
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.util.*

@RestController
@RequestMapping("/api/hellos")
class HelloRestController() {

    @GetMapping("")
    fun helloKotlinJson(): ResponseEntity<List<HelloDto>> {
        return ResponseEntity.ok(Collections.singletonList(HelloDto("world")));
    }
}

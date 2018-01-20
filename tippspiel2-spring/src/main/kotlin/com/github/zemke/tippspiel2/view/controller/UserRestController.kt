package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.view.model.AuthenticationRequestDto
import com.github.zemke.tippspiel2.view.model.UserDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController
import java.net.URI

@RestController
@RequestMapping("/api/users")
class UserRestController(
        @Autowired private val userService: UserService
) {

    @PostMapping("")
    fun createUser(@RequestBody userDto: AuthenticationRequestDto): ResponseEntity<UserDto> {
        val persistedUser = userService.addUser(userDto.firstName, userDto.lastName, userDto.email, userDto.password)
        return ResponseEntity.created(URI.create("/api/users/${persistedUser.id}")).body(UserDto.toDto(persistedUser))
    }
}

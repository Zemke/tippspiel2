package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.view.model.AuthenticationRequestDto
import com.github.zemke.tippspiel2.view.model.UserDto
import javassist.NotFoundException
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
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

    @GetMapping("/{id}")
    fun readUser(@PathVariable("id") id: Long): ResponseEntity<UserDto> {
        return when (userService.getUser(id)) {
            null -> throw NotFoundException("User with id $id not found.")
            else -> ResponseEntity.ok(UserDto.toDto(userService.getUser(id)!!))
        }
    }
}

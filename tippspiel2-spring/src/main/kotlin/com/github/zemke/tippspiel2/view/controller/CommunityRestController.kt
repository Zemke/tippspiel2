package com.github.zemke.tippspiel2.view.controller

import com.github.zemke.tippspiel2.service.CommunityService
import com.github.zemke.tippspiel2.service.UserService
import com.github.zemke.tippspiel2.view.model.CommunityCreationDto
import com.github.zemke.tippspiel2.view.model.CommunityDto
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/communities")
class CommunityRestController {

    @Autowired private lateinit var userService: UserService
    @Autowired private lateinit var communityService: CommunityService


    @PostMapping("")
    fun createCommunity(@RequestBody communityCreationDto: CommunityCreationDto): ResponseEntity<CommunityDto> {
        val communityToBeCreated = CommunityCreationDto.fromDto(
                communityCreationDto, userService.findUsers(communityCreationDto.users))
        return ResponseEntity.status(HttpStatus.CREATED).body(
                CommunityDto.toDto(communityService.save(communityToBeCreated)))
    }
}

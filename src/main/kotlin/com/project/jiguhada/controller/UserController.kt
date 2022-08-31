package com.project.jiguhada.controller

import com.project.jiguhada.service.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@Tag(name = "User 관련 API")
@RequestMapping("/api/v1/user")
class UserController(
    private val userService: UserService
) {
    @GetMapping("/checkDuplicate")
    @Operation(summary = "사용자 ID 중복체크")
        fun checkDuplicate(@RequestParam("username") username: String): Boolean {
        return userService.checkUsernameDuplicate(username)
    }
}
package com.app.orderapi.rest

import com.app.orderapi.rest.dto.UserDto
import com.app.orderapi.security.CustomUserDetails
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/users")
class UserV1Controller {

    @GetMapping("/me")
    ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(userMapper.toUserDto(userService.validateAndGetUserByUsername(currentUser.username)))
    }
}

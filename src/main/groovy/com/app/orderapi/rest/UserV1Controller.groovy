package com.app.orderapi.rest

import com.app.orderapi.mapper.UserMapper

import com.app.orderapi.rest.dto.UserDto

import com.app.orderapi.security.CustomUserDetails

import com.app.orderapi.service.UserService

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.data.domain.PageRequest

import org.springframework.http.ResponseEntity

import org.springframework.security.core.annotation.AuthenticationPrincipal

import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController

import org.springframework.web.multipart.MultipartFile


@RestController
@RequestMapping("/api/v1/users")
class UserV1Controller {

    @Autowired
    UserService userService

    @Autowired
    UserMapper userMapper

    @GetMapping("/me")
    ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        def userDto = userMapper.toUserDto(userService.validateAndGetUserByUsername(currentUser.username))
        return ResponseEntity.ok(userDto)
    }

    @GetMapping
    ResponseEntity<Map> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        def pagingSort = PageRequest.of(page, size)
        def pageResult = userService.getUsers(pagingSort)

        def response = [:]

        response["currentPage"] = pageResult.number
        response["totalItems"] = pageResult.totalElements
        response["itemsPerPage"] = pageResult.size
        response["totalPages"] = pageResult.totalPages
        response["users"] = pageResult.collect(userMapper::toUserDto)

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{username}")
    ResponseEntity<UserDto> getUser(@PathVariable String username) {
        def userDto = userMapper.toUserDto(userService.validateAndGetUserByUsername(username))

        return ResponseEntity.ok(userDto)
    }

    @DeleteMapping("/{username}")
    ResponseEntity<UserDto> deleteUser(@PathVariable String username) {
        def user = userService.validateAndGetUserByUsername(username)
        userService.deleteUser(user)

        return ResponseEntity.ok(userMapper.toUserDto(user))
    }

    @PostMapping("/profile-picture")
    ResponseEntity<Void> uploadProfilePicture(
        @RequestPart("profile-picture") MultipartFile file,
        @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            def user = userService.validateAndGetUserByUsername(currentUser.username)
            user.profilePicture = file.bytes

            userService.saveUser(user)

            return ResponseEntity.ok().build()
        } catch (Exception e) {
            return ResponseEntity.badRequest().build()
        }
    }
}

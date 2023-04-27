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
    ResponseEntity<UserDto> findMe(@AuthenticationPrincipal CustomUserDetails currentUser) {
        def userDto = userMapper.toUserDto(userService.findUserByUsernameOrEmail(currentUser.username))
        return ResponseEntity.ok(userDto)
    }

    @GetMapping
    ResponseEntity<Map> findAllUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(value = "searchQuery", required = false) String text
    ) {
        def empty = text == null || text.isEmpty()

        def paging = PageRequest.of(page, size)

        def pageResult = empty ? userService.findAllUsersPaged(paging) : userService.findAllUsersByTextPaged(paging, text)

        def response = [:]

        response["users"] = pageResult.collect(userMapper::toUserDto)
        response["currentPage"] = pageResult.number
        response["totalItems"] = pageResult.totalElements
        response["itemsPerPage"] = pageResult.size
        response["totalPages"] = pageResult.totalPages

        return ResponseEntity.ok(response)
    }

    @GetMapping("/{username}")
    ResponseEntity<UserDto> findUser(@PathVariable String username) {
        def userDto = userMapper.toUserDto(userService.findUserByUsernameOrEmail(username))

        return ResponseEntity.ok(userDto)
    }

    @DeleteMapping("/{username}")
    ResponseEntity<UserDto> deleteUser(@PathVariable String username) {
        def user = userService.findUserByUsernameOrEmail(username)
        userService.deleteUser(user)

        return ResponseEntity.ok(userMapper.toUserDto(user))
    }

    @PostMapping("/profile-picture")
    ResponseEntity<Void> uploadProfilePicture(
        @RequestPart("profile-picture") MultipartFile file,
        @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            def user = userService.findUserByUsernameOrEmail(currentUser.username)
            user.profilePicture = file.bytes

            userService.saveUser(user)

            return ResponseEntity.ok().build()
        } catch (Exception e) {
            e.printStackTrace()
            return ResponseEntity.badRequest().build()
        }
    }
}

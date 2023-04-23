package com.app.orderapi.rest

import com.app.orderapi.mapper.UserMapper
import com.app.orderapi.model.User
import com.app.orderapi.rest.dto.UserDto

import com.app.orderapi.security.CustomUserDetails

import com.app.orderapi.service.UserService

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.http.ResponseEntity

import org.springframework.security.core.annotation.AuthenticationPrincipal

import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

import java.util.stream.Collectors

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
}

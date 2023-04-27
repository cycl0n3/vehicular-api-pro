package com.app.orderapi.rest

import com.app.orderapi.exception.DuplicatedUserInfoException
import com.app.orderapi.exception.UserNotFoundException

import com.app.orderapi.model.User

import com.app.orderapi.rest.dto.AuthResponse
import com.app.orderapi.rest.dto.LoginRequest
import com.app.orderapi.rest.dto.SignUpRequest

import com.app.orderapi.security.TokenProvider
import com.app.orderapi.security.WebSecurityConfig
import com.app.orderapi.service.UserService

import jakarta.validation.Valid

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken

import org.springframework.security.crypto.password.PasswordEncoder

import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/auth")
class AuthV1Controller {

    @Autowired
    UserService userService

    @Autowired
    PasswordEncoder passwordEncoder

    @Autowired
    AuthenticationManager authenticationManager

    @Autowired
    TokenProvider tokenProvider

    @PostMapping("/authenticate")
    ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest loginRequest) {
        def token = authenticateAndGetToken(loginRequest.username, loginRequest.password)

        return ResponseEntity.ok(new AuthResponse(token))
    }

    @PostMapping("/register")
    ResponseEntity<AuthResponse> signUp(@Valid @RequestBody SignUpRequest signUpRequest) {
        if (userService.existsByUsername(signUpRequest.username)) {
            throw new DuplicatedUserInfoException(String.format("Username %s already been used", signUpRequest.username))
        }

        if (userService.existsByEmail(signUpRequest.email)) {
            throw new DuplicatedUserInfoException(String.format("Email %s already been used", signUpRequest.email))
        }

        userService.saveUser(mapSignUpRequestToUser(signUpRequest))

        def token = authenticateAndGetToken(signUpRequest.username, signUpRequest.password)

        return new ResponseEntity<AuthResponse>(new AuthResponse(token), HttpStatus.CREATED)
    }

    private String authenticateAndGetToken(String username, String password) {
        // validate username, password to be not null and password length between 4 and 16

        if(username == null || password == null || password.length() < 4 || password.length() > 16) {
            throw new UserNotFoundException("Invalid username or password")
        }

        def authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password))

        return tokenProvider.generate(authentication)
    }

    private User mapSignUpRequestToUser(SignUpRequest signUpRequest) {
        def user = new User()

        user.username = signUpRequest.username
        user.password = passwordEncoder.encode(signUpRequest.password)
        user.name = signUpRequest.name
        user.email = signUpRequest.email
        user.role = WebSecurityConfig.USER
        user.age = signUpRequest.age
        user.title = signUpRequest.title

        return user
    }
}

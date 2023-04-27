package com.app.orderapi.rest;

import com.app.orderapi.config.SwaggerConfig;
import com.app.orderapi.mapper.UserMapper;
import com.app.orderapi.model.User;
import com.app.orderapi.rest.dto.UserDto;
import com.app.orderapi.security.CustomUserDetails;
import com.app.orderapi.service.UserService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

import lombok.RequiredArgsConstructor;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return ResponseEntity.ok(userMapper.toUserDto(userService.findUserByUsernameOrEmail(currentUser.getUsername())));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public ResponseEntity<Map<String, Object>> getUsers(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        PageRequest pagingSort = PageRequest.of(page, size);
        Page<User> pageResult = userService.findAllUsersPaged(pagingSort);

        Map<String, Object> response = new HashMap<>();

        response.put("users", pageResult.getContent().stream().map(userMapper::toUserDto).collect(Collectors.toList()));
        response.put("currentPage", pageResult.getNumber());
        response.put("totalItems", pageResult.getTotalElements());
        response.put("itemsPerPage", pageResult.getSize());
        response.put("totalPages", pageResult.getTotalPages());

        return ResponseEntity.ok(response);
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/{username}")
    public ResponseEntity<UserDto> getUser(@PathVariable String username) {
        UserDto userDto = userMapper.toUserDto(userService.findUserByUsernameOrEmail(username));

        return ResponseEntity.ok(userDto);
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{username}")
    public ResponseEntity<UserDto> deleteUser(@PathVariable String username) {
        User user = userService.findUserByUsernameOrEmail(username);
        userService.deleteUser(user);

        return ResponseEntity.ok(userMapper.toUserDto(user));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)})
    @PostMapping("/profile-picture")
    public ResponseEntity<Void> uploadProfilePicture(
        @RequestPart("profile-picture") MultipartFile file,
        @AuthenticationPrincipal CustomUserDetails currentUser) {
        try {
            User user = userService.findUserByUsernameOrEmail(currentUser.getUsername());
            user.setProfilePicture(file.getBytes());
            userService.saveUser(user);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}

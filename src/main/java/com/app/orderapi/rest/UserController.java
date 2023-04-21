package com.app.orderapi.rest;

import com.app.orderapi.config.SwaggerConfig;
import com.app.orderapi.mapper.UserMapper;
import com.app.orderapi.model.User;
import com.app.orderapi.security.CustomUserDetails;
import com.app.orderapi.service.UserService;
import com.app.orderapi.rest.dto.UserDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.apache.commons.fileupload.FileItemIterator;
import org.apache.commons.fileupload.FileItemStream;
import org.apache.commons.io.IOUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import javax.servlet.http.HttpServletRequest;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;
    private final UserMapper userMapper;

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/me")
    public UserDto getCurrentUser(@AuthenticationPrincipal CustomUserDetails currentUser) {
        return userMapper.toUserDto(userService.validateAndGetUserByUsername(currentUser.getUsername()));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping
    public List<UserDto> getUsers() {
        return userService.getUsers().stream()
                .map(userMapper::toUserDto)
                .collect(Collectors.toList());
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)})
    @GetMapping("/{username}")
    public UserDto getUser(@PathVariable String username) {
        return userMapper.toUserDto(userService.validateAndGetUserByUsername(username));
    }

    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)})
    @DeleteMapping("/{username}")
    public UserDto deleteUser(@PathVariable String username) {
        User user = userService.validateAndGetUserByUsername(username);
        userService.deleteUser(user);
        return userMapper.toUserDto(user);
    }

    // file upload for the user's profile picture
    // https://www.baeldung.com/spring-file-upload
    // https://www.baeldung.com/spring-mvc-image-media-data
    @Operation(security = {@SecurityRequirement(name = SwaggerConfig.BEARER_KEY_SECURITY_SCHEME)})
    @PostMapping("/profilePicture")
    public ResponseEntity<Void> uploadProfilePicture(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                     HttpServletRequest request) {
        try {
            if (!ServletFileUpload.isMultipartContent(request)) {
                return ResponseEntity.badRequest().build();
            }

            FileItemIterator itemIterator = new ServletFileUpload().getItemIterator(request);
            FileItemStream item = itemIterator.next();

            ByteArrayOutputStream baos = new ByteArrayOutputStream();

            try (InputStream in = item.openStream()) {
                IOUtils.copy(in, baos);
            }

            User user = userService.validateAndGetUserByUsername(currentUser.getUsername());
            user.setProfilePicture(baos.toByteArray());
            userService.saveUser(user);

            IOUtils.closeQuietly(baos);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.badRequest().build();
        }
    }
}

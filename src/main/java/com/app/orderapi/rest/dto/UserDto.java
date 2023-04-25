package com.app.orderapi.rest.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record UserDto(Long id, String username, String name, String email, String role, Long orderCount,
                      String profilePicture, String title, int age) {

    public record OrderDto(String id, String description, ZonedDateTime createdAt, int status) {
    }

}
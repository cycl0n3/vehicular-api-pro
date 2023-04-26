package com.app.orderapi.rest.dto;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

public record UserDto(Long id, String username, String name, String email, String role,
                      Map<String, Long> orderCounts,
                      String profilePicture, String title, int age) {

    public record OrderDto(String id, String description, ZonedDateTime createdAt, int status) {
    }

}
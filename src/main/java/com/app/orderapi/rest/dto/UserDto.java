package com.app.orderapi.rest.dto;

import java.time.ZonedDateTime;
import java.util.List;

public record UserDto(Long id, String username, String name, String email, String role, List<OrderDto> orders,
                      String profilePicture, String title, int age) {

    public record OrderDto(String id, String description, ZonedDateTime createdAt) {
    }
}
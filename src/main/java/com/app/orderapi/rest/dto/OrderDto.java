package com.app.orderapi.rest.dto;

import java.time.ZonedDateTime;

public record OrderDto(String id, String description, OrderDto.UserDto user, ZonedDateTime createdAt, int status) {

    public record UserDto(String username) {
    }
}
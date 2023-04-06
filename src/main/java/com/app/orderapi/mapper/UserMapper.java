package com.app.orderapi.mapper;

import com.app.orderapi.model.User;
import com.app.orderapi.rest.dto.UserDto;

public interface UserMapper {

    UserDto toUserDto(User user);
}
package com.app.orderapi.mapper;

import com.app.orderapi.model.Order;
import com.app.orderapi.model.User;

import com.app.orderapi.rest.dto.UserDto;

import com.app.orderapi.service.OrderService;

import org.apache.commons.compress.utils.Lists;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class UserMapperImpl implements UserMapper {

    @Autowired
    OrderService orderService;

    @Override
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        //List<UserDto.OrderDto> orders = user.getOrders().stream().map(this::toUserDtoOrderDto).toList();
        List<UserDto.OrderDto> orders = Lists.newArrayList();

        Long orderCount = orderService.getNumberOfOrdersByUser(user);

        byte[] profilePicture = user.getProfilePicture();

        if (profilePicture != null) {
            String base64ProfilePicture = java.util.Base64.getEncoder().encodeToString(profilePicture);

            return new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole(),
                orderCount, base64ProfilePicture, user.getTitle(), user.getAge());
        }

        return new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole(),
            orderCount, null, user.getTitle(), user.getAge());
    }

    private UserDto.OrderDto toUserDtoOrderDto(Order order) {
        if (order == null) {
            return null;
        }

        return new UserDto.OrderDto(order.getId(), order.getDescription(), order.getCreatedAt(), order.getStatus());
    }
}

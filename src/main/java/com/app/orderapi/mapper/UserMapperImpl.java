package com.app.orderapi.mapper;

import com.app.orderapi.model.Order;
import com.app.orderapi.model.User;

import com.app.orderapi.rest.dto.UserDto;

import com.app.orderapi.service.OrderService;

import org.apache.commons.compress.utils.Lists;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class UserMapperImpl implements UserMapper {

    @Autowired
    OrderService orderService;

    @Override
    public UserDto toUserDto(User user) {
        if (user == null) {
            return null;
        }

        Long orderCount = orderService.getNumberOfOrdersByUser(user);

        Long acceptedOrderCount = orderService.getNumberOfAcceptedOrdersByUser(user);
        Long rejectedOrderCount = orderService.getNumberOfRejectedOrdersByUser(user);
        Long pendingOrderCount = orderService.getNumberOfPendingOrdersByUser(user);

        Map<String, Long> orderCounts = Map.of("orderCount", orderCount,
            "acceptedOrderCount", acceptedOrderCount,
            "rejectedOrderCount", rejectedOrderCount,
            "pendingOrderCount", pendingOrderCount);

        byte[] profilePicture = user.getProfilePicture();

        if (profilePicture != null) {
            String base64ProfilePicture = java.util.Base64.getEncoder().encodeToString(profilePicture);

            return new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole(),
                orderCounts,
                base64ProfilePicture, user.getTitle(), user.getAge());
        }

        return new UserDto(user.getId(), user.getUsername(), user.getName(), user.getEmail(), user.getRole(),
            orderCounts,
            null, user.getTitle(), user.getAge());
    }

    private UserDto.OrderDto toUserDtoOrderDto(Order order) {
        if (order == null) {
            return null;
        }

        return new UserDto.OrderDto(order.getId(), order.getDescription(), order.getCreatedAt(), order.getStatus());
    }
}

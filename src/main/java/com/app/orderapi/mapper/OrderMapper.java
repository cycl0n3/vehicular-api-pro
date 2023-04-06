package com.app.orderapi.mapper;

import com.app.orderapi.model.Order;
import com.app.orderapi.rest.dto.CreateOrderRequest;
import com.app.orderapi.rest.dto.OrderDto;

public interface OrderMapper {

    Order toOrder(CreateOrderRequest createOrderRequest);

    OrderDto toOrderDto(Order order);
}
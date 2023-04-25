package com.app.orderapi.service;

import com.app.orderapi.model.Order;
import com.app.orderapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    List<Order> getOrders();

    Long getNumberOfOrders();

    List<Order> getOrdersContainingText(String text);

    Order validateAndGetOrder(String id);

    Order saveOrder(Order order);

    void deleteOrder(Order order);

    Page<Order> getOrdersByUser(User user, PageRequest pagingSort);

    Long getNumberOfOrdersByUser(User user);
}

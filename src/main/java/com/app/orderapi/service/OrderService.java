package com.app.orderapi.service;

import com.app.orderapi.model.Order;
import com.app.orderapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    @Deprecated
    List<Order> getOrders();

    Long getNumberOfOrders();

    @Deprecated
    List<Order> getOrdersContainingText(String text);

    Order validateAndGetOrder(String id);

    Order saveOrder(Order order);

    void deleteOrder(Order order);

    Page<Order> findOrdersByUser(User user, PageRequest paging);

    Page<Order> findOrdersByUser(User user, String searchQuery, PageRequest paging);

    Long getNumberOfOrdersByUser(User user);

    Long getNumberOfAcceptedOrdersByUser(User user);

    Long getNumberOfRejectedOrdersByUser(User user);

    Long getNumberOfPendingOrdersByUser(User user);

    Page<Order> findAll(Pageable pageable);

    Page<Order> findAllByText(String text, PageRequest paging);
}

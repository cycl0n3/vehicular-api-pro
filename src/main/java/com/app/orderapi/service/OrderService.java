package com.app.orderapi.service;

import com.app.orderapi.model.Order;
import com.app.orderapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface OrderService {

    @Deprecated
    List<Order> findAllOrders();

    Long getTotalNumberOfOrders();

    @Deprecated
    List<Order> findOrdersContainingText(String text);

    Order validateAndGetOrder(String id);

    Order saveOrder(Order order);

    void deleteOrder(Order order);

    Page<Order> findAllOrdersByUserPaged(User user, PageRequest paging);

    Page<Order> findAllOrdersByUserAndTextPaged(User user, String searchQuery, PageRequest paging);

    Long getNumberOfOrdersByUser(User user);

    Long getNumberOfAcceptedOrdersByUser(User user);

    Long getNumberOfRejectedOrdersByUser(User user);

    Long getNumberOfPendingOrdersByUser(User user);

    Page<Order> findAllordersPaged(Pageable pageable);

    Page<Order> findAllOrdersByTextPaged(String text, PageRequest paging);

    Order acceptOrder(Order order);

    Order rejectOrder(Order order);
}

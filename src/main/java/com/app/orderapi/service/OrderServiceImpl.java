package com.app.orderapi.service;

import com.app.orderapi.model.User;
import com.app.orderapi.repository.OrderRepository;
import com.app.orderapi.exception.OrderNotFoundException;
import com.app.orderapi.model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    @Override
    public List<Order> getOrders() {
        return orderRepository.findAllByOrderByCreatedAtDesc();
    }

    @Override
    public Long getNumberOfOrders() {
        return orderRepository.count();
    }

    @Override
    public List<Order> getOrdersContainingText(String text) {
        return orderRepository.findByIdContainingOrDescriptionContainingIgnoreCaseOrderByCreatedAt(text, text);
    }

    @Override
    public Order validateAndGetOrder(String id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new OrderNotFoundException(String.format("Order with id %s not found", id)));
    }

    @Override
    public Order saveOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public void deleteOrder(Order order) {
        orderRepository.delete(order);
    }

    @Override
    public Page<Order> findOrdersByUser(User user, PageRequest paging) {
        return orderRepository.findAllByUserOrderByCreatedAtDesc(user, paging);
    }

    @Override
    public Page<Order> findOrdersByUser(User user, String searchQuery, PageRequest paging) {
        return orderRepository.findAllByUserAndDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(user, searchQuery, paging);
    }

    @Override
    public Long getNumberOfOrdersByUser(User user) {
        return orderRepository.countByUser(user);
    }

    @Override
    public Long getNumberOfAcceptedOrdersByUser(User user) {
        return orderRepository.countByUserAndStatus(user, Order.Status.ACCEPTED);
    }

    @Override
    public Long getNumberOfRejectedOrdersByUser(User user) {
        return orderRepository.countByUserAndStatus(user, Order.Status.REJECTED);
    }

    @Override
    public Long getNumberOfPendingOrdersByUser(User user) {
        return orderRepository.countByUserAndStatus(user, Order.Status.PENDING);
    }

    @Override
    public Page<Order> findAll(Pageable pageable) {
        return orderRepository.findAll(pageable);
    }

    @Override
    public Page<Order> findAllByText(String text, PageRequest paging) {
        return orderRepository.findAllByDescriptionContainingIgnoreCase(text, paging);
    }
}

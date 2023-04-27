package com.app.orderapi.repository;

import com.app.orderapi.model.Order;
import com.app.orderapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findAllByOrderByCreatedAtDesc();

    Page<Order> findAllByDescriptionContainingIgnoreCase(String text, PageRequest pageRequest);

    Page<Order> findAllByUserOrderByCreatedAtDesc(User user, PageRequest pageRequest);

    Page<Order> findAllByUserAndDescriptionContainingIgnoreCaseOrderByCreatedAtDesc(User user, String description, PageRequest pageRequest);

    List<Order> findByIdContainingOrDescriptionContainingIgnoreCaseOrderByCreatedAt(String id, String description);

    Long countByUser(User user);

    Long countByUserAndStatus(User user, Integer status);
}

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

    List<Order> findAllByOrderByCreatedAtDesc(PageRequest pageRequest);

    Page<Order> findAllByUserOrderByCreatedAtDesc(User user, PageRequest pageRequest);

    List<Order> findByIdContainingOrDescriptionContainingIgnoreCaseOrderByCreatedAt(String id, String description);

    Long countByUser(User user);
}

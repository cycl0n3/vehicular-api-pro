package com.app.orderapi.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Data
@NoArgsConstructor
@Entity
@Table(name = "orders")
public class Order {

    public static class Status {
        public static final int PENDING = 0;
        public static final int ACCEPTED = 1;
        public static final int REJECTED = -1;
    }

    @Id
    @Column(name = "id", nullable = false, updatable = false)
    private String id;

    @Column(nullable = false)
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", nullable = false, updatable = false)
    private ZonedDateTime createdAt;

    @Column(name = "status", nullable = false, columnDefinition = "int default 0")
    private int status;

    public Order(String description) {
        this.description = description;
    }

    @PrePersist
    public void onPrePersist() {
        createdAt = ZonedDateTime.now();
    }
}

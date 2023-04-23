package com.app.orderapi.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@Entity
@Table(name = "users", uniqueConstraints = {
        @UniqueConstraint(columnNames = "username"),
        @UniqueConstraint(columnNames = "email")
})
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, updatable = false)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String name;

    @Column(unique = true)
    private String email;

    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'USER'")
    private String role;

    @Column(columnDefinition = "LONGBLOB")
    private byte[] profilePicture;

    @Column(columnDefinition = "VARCHAR(50) DEFAULT 'Mr.'")
    private String title;

    @Column(columnDefinition = "INT(11) DEFAULT 0")
    private int age;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Order> orders = new ArrayList<>();

    public User(String username, String password, String name, String email, String role, byte[] profilePicture, String title, int age) {
        this.username = username;
        this.password = password;
        this.name = name;
        this.email = email;
        this.role = role;
        this.profilePicture = profilePicture;
        this.title = title;
        this.age = age;
    }
}

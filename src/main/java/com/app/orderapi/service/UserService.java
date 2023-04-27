package com.app.orderapi.service;

import com.app.orderapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    List<User> findAll();

    long getNumberOfUsers();

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findByUsernameOrEmail(String username);

    User saveUser(User user);

    void deleteUser(User user);

    Page<User> findAll(PageRequest paging);

    Page<User> findAll(PageRequest paging, String search);
}

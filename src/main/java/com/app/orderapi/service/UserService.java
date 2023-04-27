package com.app.orderapi.service;

import com.app.orderapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.util.List;
import java.util.Optional;

public interface UserService {

    @Deprecated
    List<User> findAllUsers();

    long getNumberOfUsers();

    Optional<User> findUserByUsername(String username);

    Optional<User> findUserByEmail(String email);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);

    User findUserByUsernameOrEmail(String username);

    User saveUser(User user);

    void deleteUser(User user);

    Page<User> findAllUsersPaged(PageRequest paging);

    Page<User> findAllUsersByTextPaged(PageRequest paging, String search);
}

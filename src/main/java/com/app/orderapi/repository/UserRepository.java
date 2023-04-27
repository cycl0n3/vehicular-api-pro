package com.app.orderapi.repository;

import com.app.orderapi.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    Page<User> findAllByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(String username, String email, PageRequest paging);

    boolean existsByUsername(String username);

    boolean existsByEmail(String email);
}

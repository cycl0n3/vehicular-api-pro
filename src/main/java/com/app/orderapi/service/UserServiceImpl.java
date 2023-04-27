package com.app.orderapi.service;

import com.app.orderapi.repository.UserRepository;
import com.app.orderapi.exception.UserNotFoundException;
import com.app.orderapi.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();
    }

    @Override
    public long getNumberOfUsers() {
        return userRepository.count();
    }

    @Override
    public Page<User> findAllUsersPaged(PageRequest paging) {
        return userRepository.findAll(paging);
    }

    @Override
    public Page<User> findAllUsersByTextPaged(PageRequest paging, String search) {
        return userRepository.findAllByUsernameContainingIgnoreCaseOrEmailContainingIgnoreCase(search, search, paging);
    }

    @Override
    public Optional<User> findUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public boolean existsByUsername(String username) {
        return userRepository.existsByUsername(username);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public User findUserByUsernameOrEmail(String username) {
        Optional<User> user = userRepository.findByUsername(username);

        if (user.isPresent()) {
            return user.get();
        } else {
            user = userRepository.findByEmail(username);

            if (user.isPresent()) {
                return user.get();
            }

            throw new UserNotFoundException(String.format("User with username %s not found", username));
        }
    }

    @Override
    public User saveUser(User user) {
        return userRepository.save(user);
    }

    @Override
    public void deleteUser(User user) {
        userRepository.delete(user);
    }
}

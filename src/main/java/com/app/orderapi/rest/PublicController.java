package com.app.orderapi.rest;

import com.app.orderapi.service.OrderService;
import com.app.orderapi.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/public")
public class PublicController {

    private final UserService userService;
    private final OrderService orderService;

    @GetMapping("/numberOfUsers")
    public ResponseEntity<Integer> getNumberOfUsers() {
        return ResponseEntity.ok(userService.findAllUsers().size());
    }

    @GetMapping("/numberOfOrders")
    public ResponseEntity<Integer> getNumberOfOrders() {
        return ResponseEntity.ok(orderService.findAllOrders().size());
    }
}

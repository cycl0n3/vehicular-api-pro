package com.app.orderapi.rest

import com.app.orderapi.mapper.OrderMapper
import com.app.orderapi.model.Order
import com.app.orderapi.model.User
import com.app.orderapi.rest.dto.CreateOrderRequest
import com.app.orderapi.rest.dto.OrderDto
import com.app.orderapi.security.CustomUserDetails
import com.app.orderapi.service.OrderService
import com.app.orderapi.service.UserService
import jakarta.validation.Valid
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/orders")
class OrderV1Controller {

    @Autowired
    UserService userService

    @Autowired
    OrderService orderService

    @Autowired
    OrderMapper orderMapper

    @GetMapping
    ResponseEntity<List> getOrders(@RequestParam(value = "text", required = false) String text) {
        def orders = (text == null) ? orderService.findAll() : orderService.getOrdersContainingText(text)

        return ResponseEntity.ok(orders.collect(orderMapper::toOrderDto))
    }

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    ResponseEntity<OrderDto> createOrder(@AuthenticationPrincipal CustomUserDetails currentUser,
                                                @Valid @RequestBody CreateOrderRequest createOrderRequest) {
        def user = userService.validateAndGetUserByUsername(currentUser.username)
        def order = orderMapper.toOrder(createOrderRequest)

        order.id = UUID.randomUUID().toString()
        order.user = user

        return ResponseEntity.ok(orderMapper.toOrderDto(orderService.saveOrder(order)))
    }
}

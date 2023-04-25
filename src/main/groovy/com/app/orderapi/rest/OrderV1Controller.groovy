package com.app.orderapi.rest

import com.app.orderapi.mapper.OrderMapper

import com.app.orderapi.rest.dto.CreateOrderRequest
import com.app.orderapi.rest.dto.OrderDto

import com.app.orderapi.security.CustomUserDetails

import com.app.orderapi.service.OrderService
import com.app.orderapi.service.UserService

import jakarta.validation.Valid

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.domain.PageRequest
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity

import org.springframework.security.core.annotation.AuthenticationPrincipal

import org.springframework.web.bind.annotation.*

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

    @GetMapping("/me")
    ResponseEntity<Map> getMyOrders(
        @AuthenticationPrincipal CustomUserDetails currentUser,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size
    ) {
        def user = userService.validateAndGetUserByUsername(currentUser.username)

        def pagingSort = PageRequest.of(page, size)
        def pageResult = orderService.getOrdersByUser(user, pagingSort)

        def response = [:]

        response['orders'] = pageResult.content.collect(orderMapper::toOrderDto)
        response['currentPage'] = pageResult.number
        response['totalItems'] = pageResult.totalElements
        response['itemsPerPage'] = pageResult.size
        response['totalPages'] = pageResult.totalPages

        return ResponseEntity.ok(response)
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

    @DeleteMapping("/{id}")
    ResponseEntity<OrderDto> deleteOrders(@PathVariable UUID id) {
        def order = orderService.validateAndGetOrder(id.toString())
        orderService.deleteOrder(order)

        return ResponseEntity.ok(orderMapper.toOrderDto(order))
    }
}

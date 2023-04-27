package com.app.orderapi.rest

import com.app.orderapi.mapper.OrderMapper
import com.app.orderapi.model.Order
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
    ResponseEntity<Map> findAllOrders(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(value = "searchQuery", required = false) String text
    ) {
        def empty = text == null || text.isEmpty()

        def paging = PageRequest.of(page, size)

        def pageResult = empty ? orderService.findAllordersPaged(paging) : orderService.findAllOrdersByTextPaged(text, paging)

        def response = [:]

        response['orders'] = pageResult.content.collect(orderMapper::toOrderDto)
        response['currentPage'] = pageResult.number
        response['totalItems'] = pageResult.totalElements
        response['itemsPerPage'] = pageResult.size
        response['totalPages'] = pageResult.totalPages

        return ResponseEntity.ok(response)
    }

    @GetMapping("/me")
    ResponseEntity<Map> findMyOrders(
        @AuthenticationPrincipal CustomUserDetails currentUser,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @RequestParam(value = "searchQuery", required = false) String text
    ) {
        def empty = text == null || text.isEmpty()

        def user = userService.findUserByUsernameOrEmail(currentUser.username)

        def paging = PageRequest.of(page, size)

        def pageResult = empty ? orderService.findAllOrdersByUserPaged(user, paging) : orderService.findAllOrdersByUserAndTextPaged(user, text, paging)

        def response = [:]

        response['orders'] = pageResult.content.collect(orderMapper::toOrderDto)
        response['currentPage'] = pageResult.number
        response['totalItems'] = pageResult.totalElements
        response['itemsPerPage'] = pageResult.size
        response['totalPages'] = pageResult.totalPages

        return ResponseEntity.ok(response)
    }

    @GetMapping("/other/{username}")
    ResponseEntity<Map> findOrdersByUser(
        @AuthenticationPrincipal CustomUserDetails currentUser,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "10") int size,
        @PathVariable String username,
        @RequestParam(value = "searchQuery", required = false) String text
    ) {
        def empty = text == null || text.isEmpty()

        def user = userService.findUserByUsernameOrEmail(username)

        def paging = PageRequest.of(page, size)

        def pageResult = empty ? orderService.findAllOrdersByUserPaged(user, paging) : orderService.findAllOrdersByUserAndTextPaged(user, text, paging)

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
    ResponseEntity<OrderDto> createOrder(
        @AuthenticationPrincipal CustomUserDetails currentUser,
        @Valid @RequestBody CreateOrderRequest createOrderRequest
    ) {
        def user = userService.findUserByUsernameOrEmail(currentUser.username)
        def order = orderMapper.toOrder(createOrderRequest)

        order.id = UUID.randomUUID().toString()
        order.user = user

        return ResponseEntity.ok(orderMapper.toOrderDto(orderService.saveOrder(order)))
    }

    @PostMapping("/{username}/create")
    ResponseEntity<OrderDto> createOrderForUser(
        @AuthenticationPrincipal CustomUserDetails currentUser,
        @PathVariable String username,
        @Valid @RequestBody CreateOrderRequest createOrderRequest
    ) {
        def user = userService.findUserByUsernameOrEmail(username)
        def order = orderMapper.toOrder(createOrderRequest)

        order.id = UUID.randomUUID().toString()
        order.user = user

        return ResponseEntity.ok(orderMapper.toOrderDto(orderService.saveOrder(order)))
    }

    @DeleteMapping("/{id}")
    ResponseEntity<OrderDto> deleteOrders(
        @PathVariable UUID id
    ) {
        def order = orderService.validateAndGetOrder(id.toString())
        orderService.deleteOrder(order)

        return ResponseEntity.ok(orderMapper.toOrderDto(order))
    }

    @PostMapping("/{id}/accept")
    ResponseEntity<OrderDto> acceptOrder(
        @PathVariable UUID id
    ) {
        def order = orderService.validateAndGetOrder(id.toString())
        order = orderService.acceptOrder(order)

        return ResponseEntity.ok(orderMapper.toOrderDto(order))
    }

    @PostMapping("/{id}/reject")
    ResponseEntity<OrderDto> rejectOrder(
        @PathVariable UUID id
    ) {
        def order = orderService.validateAndGetOrder(id.toString())
        order = orderService.rejectOrder(order)

        return ResponseEntity.ok(orderMapper.toOrderDto(order))
    }
}

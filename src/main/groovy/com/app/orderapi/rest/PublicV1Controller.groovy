package com.app.orderapi.rest

import com.app.orderapi.service.OrderService
import com.app.orderapi.service.UserService

import org.springframework.beans.factory.annotation.Autowired

import org.springframework.http.ResponseEntity

import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/public")
class PublicV1Controller {

    @Autowired
    UserService userService

    @Autowired
    OrderService orderService

    @RequestMapping("/numberOfUsers")
    ResponseEntity<Map> getNumberOfUsers() {
        Map response = [:]
        response['numberOfUsers'] = userService.numberOfUsers

        return ResponseEntity.ok(response)
    }

    @RequestMapping("/numberOfOrders")
    ResponseEntity<Map> getNumberOfOrders() {
        Map response = [:]
        response['numberOfOrders'] = orderService.totalNumberOfOrders

        return ResponseEntity.ok(response)
    }
}

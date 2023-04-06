package com.app.orderapi;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class OrderApiApplication extends ServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(OrderApiApplication.class, args);
    }
}

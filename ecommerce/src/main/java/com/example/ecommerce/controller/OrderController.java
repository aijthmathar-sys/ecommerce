package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    
    @PostMapping("/place")
    public Order placeOrder(@RequestParam Long userId) {
        return orderService.placeOrder(userId);
    }

    
    @GetMapping("/{userId}")
    public List<Order> getOrders(@PathVariable Long userId) {
        return orderService.getOrdersByUserId(userId);
    }
}
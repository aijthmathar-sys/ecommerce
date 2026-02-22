package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.OrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
@CrossOrigin("*")
public class OrderController {

    @Autowired
    private OrderService orderService;

    
 @PostMapping("/place")
public Order placeOrder(Authentication authentication) {

    User user = (User) authentication.getPrincipal();

    return orderService.placeOrder(user.getId());
}

    
   @GetMapping("/my")
public List<Order> getMyOrders(Authentication authentication) {
    User user = (User) authentication.getPrincipal();
    return orderService.getOrdersByUserId(user.getId());
}
}
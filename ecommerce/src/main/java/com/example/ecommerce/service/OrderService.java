package com.example.ecommerce.service;

import com.example.ecommerce.entity.Order;
import java.util.List;

public interface OrderService {

    Order placeOrder(Long userId);

    List<Order> getOrdersByUserId(Long userId);
}
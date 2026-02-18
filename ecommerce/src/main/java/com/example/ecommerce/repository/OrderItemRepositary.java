package com.example.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.entity.OrderItem;

public interface OrderItemRepositary extends JpaRepository<OrderItem,Long> {
    
}

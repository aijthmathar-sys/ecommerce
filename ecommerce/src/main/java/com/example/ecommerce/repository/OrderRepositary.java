package com.example.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.entity.Order;

public interface OrderRepositary extends JpaRepository<Order,Long>{
    List<Order> findByUserId(Long userid);
    
}

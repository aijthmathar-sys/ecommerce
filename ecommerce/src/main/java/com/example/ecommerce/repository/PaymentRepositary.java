package com.example.ecommerce.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.example.ecommerce.entity.Payment;

public interface PaymentRepositary extends JpaRepository<Payment,Long> {
    Optional<Payment> findByRazorpayOrderId(String razorpayOrderId);
    
}

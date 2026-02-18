package com.example.ecommerce.service;

public interface PaymentService {

    String createRazorpayOrder(Long orderId) throws Exception;

    String verifyPayment(String razorpayOrderId,
                         String razorpayPaymentId,
                         String razorpaySignature) throws Exception;
}
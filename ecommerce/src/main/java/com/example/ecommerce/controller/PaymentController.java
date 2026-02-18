package com.example.ecommerce.controller;

import com.example.ecommerce.service.PaymentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@CrossOrigin("*")
public class PaymentController {

    @Autowired
    private PaymentService paymentService;

    // 🔥 Create Razorpay Order
    @PostMapping("/create/{orderId}")
    public String createOrder(@PathVariable Long orderId) throws Exception {
        return paymentService.createRazorpayOrder(orderId);
    }

    // 🔥 Verify Payment
    @PostMapping("/verify")
    public String verifyPayment(
            @RequestParam String razorpayOrderId,
            @RequestParam String razorpayPaymentId,
            @RequestParam String razorpaySignature) throws Exception {

        return paymentService.verifyPayment(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature);
    }
}
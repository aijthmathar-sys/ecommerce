package com.example.ecommerce.service;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.Payment;
import com.example.ecommerce.repository.OrderRepositary;

import com.example.ecommerce.repository.PaymentRepositary;

import com.razorpay.RazorpayClient;
import com.razorpay.Utils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
public class PaymentServiceImpl implements PaymentService {

    @Autowired
    private RazorpayClient razorpayClient;

    @Autowired
    private PaymentRepositary paymentRepository;

    @Autowired
    private OrderRepositary orderRepository;

    @Value("${razorpay.key.secret}")
    private String keySecret;

    // 🔥 Step 1: Create Razorpay Order
    @Override
    public String createRazorpayOrder(Long orderId) throws Exception {

        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        JSONObject options = new JSONObject();
        options.put("amount", order.getTotalAmount() * 100); // paise
        options.put("currency", "INR");
        options.put("receipt", "order_" + orderId);

        com.razorpay.Order razorpayOrder =
                razorpayClient.orders.create(options);

        Payment payment = new Payment();
        payment.setOrderId(orderId);
        payment.setUserId(order.getUserId());
        payment.setAmount(order.getTotalAmount());
        payment.setStatus("CREATED");
        payment.setRazorpayOrderId(razorpayOrder.get("id"));
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        return razorpayOrder.get("id");
    }

    // 🔥 Step 2: Verify Payment Signature
    @Override
    public String verifyPayment(String razorpayOrderId,
                                String razorpayPaymentId,
                                String razorpaySignature) throws Exception {

        JSONObject attributes = new JSONObject();
        attributes.put("razorpay_order_id", razorpayOrderId);
        attributes.put("razorpay_payment_id", razorpayPaymentId);
        attributes.put("razorpay_signature", razorpaySignature);

        boolean isValid =
                Utils.verifyPaymentSignature(attributes, keySecret);

        if (!isValid) {
            return "Invalid Signature";
        }

        Payment payment = paymentRepository
                .findByRazorpayOrderId(razorpayOrderId)
                .orElseThrow(() -> new RuntimeException("Payment not found"));

        payment.setRazorpayPaymentId(razorpayPaymentId);
        payment.setRazorpaySignature(razorpaySignature);
        payment.setStatus("SUCCESS");
        payment.setPaymentDate(LocalDateTime.now());

        paymentRepository.save(payment);

        // ✅ Update Order Status
        Order order = orderRepository
                .findById(payment.getOrderId())
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus("PAID");
        orderRepository.save(order);

        return "Payment Verified Successfully";

    }
    // This is required so the test can spy and mock it
protected com.razorpay.Order createOrder(JSONObject options) throws Exception {
    return razorpayClient.orders.create(options);
}

}
package com.example.ecommerce.controller;

import com.example.ecommerce.service.PaymentService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    // ================= CREATE RAZORPAY ORDER =================
    @Test
    void createOrder_shouldReturnOrderResponse() throws Exception {

        Long orderId = 1L;
        String mockResponse = "razorpay_order_created";

        when(paymentService.createRazorpayOrder(orderId))
                .thenReturn(mockResponse);

        String result = paymentController.createOrder(orderId);

        assertNotNull(result);
        assertEquals(mockResponse, result);

        verify(paymentService).createRazorpayOrder(orderId);
    }

    // ================= VERIFY PAYMENT =================
    @Test
    void verifyPayment_shouldReturnVerificationResult() throws Exception {

        String razorpayOrderId = "order_123";
        String razorpayPaymentId = "payment_123";
        String razorpaySignature = "signature_abc";

        String mockResponse = "Payment Verified";

        when(paymentService.verifyPayment(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature))
                .thenReturn(mockResponse);

        String result = paymentController.verifyPayment(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature);

        assertNotNull(result);
        assertEquals(mockResponse, result);

        verify(paymentService).verifyPayment(
                razorpayOrderId,
                razorpayPaymentId,
                razorpaySignature);
    }
}
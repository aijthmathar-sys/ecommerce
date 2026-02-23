package com.example.ecommerce.service;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.Payment;
import com.example.ecommerce.repository.OrderRepositary;
import com.example.ecommerce.repository.PaymentRepositary;
import com.razorpay.RazorpayClient;
import com.razorpay.Utils;

import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PaymentServiceImplTest {

    @Mock
    private RazorpayClient razorpayClient;

    @Mock
    private PaymentRepositary paymentRepository;

    @Mock
    private OrderRepositary orderRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Order order;

    @BeforeEach
    void setUp() {
        order = new Order();
        order.setId(1L);
        order.setUserId(10L);
        order.setTotalAmount(1000.0);
        order.setStatus("CREATED");

        // Inject Razorpay secret key for tests
        ReflectionTestUtils.setField(paymentService, "keySecret", "test_secret");
    }

   
    @Test
    void testCreateRazorpayOrder_OrderNotFound() {
        when(orderRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> paymentService.createRazorpayOrder(1L));
    }

    // =============================
    // TEST: VERIFY PAYMENT SUCCESS
    // =============================
    @Test
    void testVerifyPayment_Success() throws Exception {

        Payment payment = new Payment();
        payment.setOrderId(1L);
        payment.setRazorpayOrderId("razor_order_123");

        when(paymentRepository.findByRazorpayOrderId("razor_order_123"))
                .thenReturn(Optional.of(payment));

        when(orderRepository.findById(1L)).thenReturn(Optional.of(order));

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {

            mockedUtils.when(() ->
                    Utils.verifyPaymentSignature(any(JSONObject.class), anyString()))
                    .thenReturn(true);

            String result = paymentService.verifyPayment(
                    "razor_order_123",
                    "payment_123",
                    "signature_123");

            assertEquals("Payment Verified Successfully", result);
            verify(paymentRepository).save(payment);
            verify(orderRepository).save(order);
        }
    }

    // =============================
    // TEST: VERIFY PAYMENT INVALID SIGNATURE
    // =============================
    @Test
    void testVerifyPayment_InvalidSignature() throws Exception {

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {

            mockedUtils.when(() ->
                    Utils.verifyPaymentSignature(any(JSONObject.class), anyString()))
                    .thenReturn(false);

            String result = paymentService.verifyPayment(
                    "razor_order_123",
                    "payment_123",
                    "signature_123");

            assertEquals("Invalid Signature", result);
        }
    }

    // =============================
    // TEST: VERIFY PAYMENT PAYMENT NOT FOUND
    // =============================
    @Test
    void testVerifyPayment_PaymentNotFound() throws Exception {

        try (MockedStatic<Utils> mockedUtils = mockStatic(Utils.class)) {

            mockedUtils.when(() ->
                    Utils.verifyPaymentSignature(any(JSONObject.class), anyString()))
                    .thenReturn(true);

            when(paymentRepository.findByRazorpayOrderId("razor_order_123"))
                    .thenReturn(Optional.empty());

            assertThrows(RuntimeException.class,
                    () -> paymentService.verifyPayment(
                            "razor_order_123",
                            "payment_123",
                            "signature_123"));
        }
    }
}

package com.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Razorpay Order ID is required")
    private String razorpayOrderId;

    @NotBlank(message = "Razorpay Payment ID is required")
    private String razorpayPaymentId;

    @NotBlank(message = "Razorpay Signature is required")
    private String razorpaySignature;

    @Positive(message = "Amount must be greater than 0")
    private double amount;

    @NotBlank(message = "Payment status is required")
    private String status;

    private LocalDateTime paymentDate;

    @NotNull(message = "User ID is required")
    private Long userId;

    @NotNull(message = "Order ID is required")
    private Long orderId;
}
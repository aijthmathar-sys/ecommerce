package com.example.ecommerce.entity;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "Quantity must be greater than 0")
    private int quantity;

    @Positive(message = "Price must be greater than 0")
    private double price;

    @ManyToOne
    @JoinColumn(name = "orderid", nullable = false)
    private Order order;

    @ManyToOne
    @JoinColumn(name = "productid", nullable = false)
    private Product product;
}
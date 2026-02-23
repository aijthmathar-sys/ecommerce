package com.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "CARTITEMS")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CartItem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Positive(message = "Quantity must be greater than 0")
    private int quantity;

    @ManyToOne
    @JoinColumn(name = "cartid", nullable = false)
    private Cart cart;

    @ManyToOne
    @JoinColumn(name = "productid", nullable = false)
    private Product product;
}
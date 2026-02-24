package com.example.ecommerce.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;

@Entity
@Table(name = "PRODUCTS")
@NoArgsConstructor
@AllArgsConstructor
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Product name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Description is required")
    private String description;

    @Positive(message = "Price must be greater than 0")
    @Column(nullable = false)
    private double price;

    @PositiveOrZero(message = "Stock cannot be negative")
    private int stock;

    @NotBlank(message = "Image URL is required")
    private String imageurl;

    @Enumerated(EnumType.STRING)
    @NotNull(message = "Category is required")
    @Column(nullable = false)
    private Category category;
}

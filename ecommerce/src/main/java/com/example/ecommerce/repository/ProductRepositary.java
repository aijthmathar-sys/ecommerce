package com.example.ecommerce.repository;

import org.springdoc.core.converters.models.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.ecommerce.entity.Product;

@Repository
public interface ProductRepositary extends JpaRepository<Product,Long> {
    Page<Product> findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
        String name,
        String Description,
        org.springframework.data.domain.Pageable pageable
    );

    
} 
    


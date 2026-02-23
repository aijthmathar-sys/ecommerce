package com.example.ecommerce.service;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepositary;
import com.example.ecommerce.service.ProductService;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.*;
 
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepositary productRepositary;

    @InjectMocks
    private ProductService productService;

    private Product product;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Laptop");
        product.setDescription("Gaming Laptop");
        product.setPrice(50000.0);
        product.setStock(10);
        product.setImageurl("image.jpg");
    }

    @Test
    void testAddProduct() {
        when(productRepositary.save(product)).thenReturn(product);

        Product saved = productService.addProduct(product);

        assertNotNull(saved);
        assertEquals("Laptop", saved.getName());
        verify(productRepositary, times(1)).save(product);
    }

    @Test
    void testGetAllProducts() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepositary.findAll(pageable)).thenReturn(page);

        Page<Product> result = productService.getAllProducts(0, 5);

        assertEquals(1, result.getTotalElements());
        verify(productRepositary).findAll(pageable);
    }

    @Test
    void testGetProductById_Success() {
        when(productRepositary.findById(1L)).thenReturn(Optional.of(product));

        Product found = productService.getProductById(1L);

        assertEquals(1L, found.getId());
        verify(productRepositary).findById(1L);
    }

    @Test
    void testGetProductById_NotFound() {
        when(productRepositary.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> productService.getProductById(1L));
    }

    @Test
    void testUpdateProduct() {
        Product updated = new Product();
        updated.setName("Updated Laptop");
        updated.setDescription("Updated Desc");
        updated.setPrice(60000.0);
        updated.setStock(5);
        updated.setImageurl("updated.jpg");

        when(productRepositary.findById(1L)).thenReturn(Optional.of(product));
        when(productRepositary.save(any(Product.class))).thenReturn(product);

        Product result = productService.updateProduct(1L, updated);

        assertEquals("Updated Laptop", result.getName());
        verify(productRepositary).save(product);
    }

    @Test
    void testSearchProducts() {
        Pageable pageable = PageRequest.of(0, 5, Sort.by("id").descending());
        Page<Product> page = new PageImpl<>(List.of(product));

        when(productRepositary
                .findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        "Laptop", "Laptop", pageable))
                .thenReturn(page);

        Page<Product> result = productService.searchProducts("Laptop", 0, 5);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void testDeleteProduct() {
        when(productRepositary.findById(1L)).thenReturn(Optional.of(product));
        doNothing().when(productRepositary).delete(product);

        productService.deleteProduct(1L);

        verify(productRepositary, times(1)).delete(product);
    }

    @Test
    void testDeleteProduct_NotFound() {
        when(productRepositary.findById(1L)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class,
                () -> productService.deleteProduct(1L));
    }
}


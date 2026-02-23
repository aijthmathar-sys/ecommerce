package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Category;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    private Product product;
    private Page<Product> productPage;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");

        productPage = new PageImpl<>(List.of(product));
    }

    // ================= ADD PRODUCT =================
    @Test
    void addProduct_shouldReturnSavedProduct() {

        when(productService.addProduct(product)).thenReturn(product);

        Product result = productController.addProduct(product);

        assertNotNull(result);
        assertEquals(product, result);

        verify(productService).addProduct(product);
    }

    // ================= GET ALL PRODUCTS =================
    @Test
    void getAllProducts_shouldApplyPaginationLimits() {

        when(productService.getAllProducts(0, 5)).thenReturn(productPage);

        Page<Product> result = productController.getAllProducts(0, 5);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(productService).getAllProducts(0, 5);
    }

    @Test
    void getAllProducts_shouldCorrectNegativeAndOversizeValues() {

        when(productService.getAllProducts(0, 20)).thenReturn(productPage);

        Page<Product> result = productController.getAllProducts(-1, 50);

        assertNotNull(result);
        verify(productService).getAllProducts(0, 20);
    }

    // ================= GET PRODUCT BY ID =================
    @Test
    void getProductById_shouldReturnProduct() {

        when(productService.getProductById(1L)).thenReturn(product);

        Product result = productController.getProductById(1L);

        assertNotNull(result);
        assertEquals(product, result);

        verify(productService).getProductById(1L);
    }

    // ================= UPDATE PRODUCT =================
    @Test
    void updateProduct_shouldReturnUpdatedProduct() {

        when(productService.updateProduct(1L, product)).thenReturn(product);

        Product result = productController.updateProduct(1L, product);

        assertNotNull(result);
        assertEquals(product, result);

        verify(productService).updateProduct(1L, product);
    }

    // ================= DELETE PRODUCT =================
    @Test
    void deleteProduct_shouldCallService() {

        productController.deleteProduct(1L);

        verify(productService).deleteProduct(1L);
    }

    // ================= SEARCH PRODUCTS =================
    @Test
    void searchProducts_shouldReturnPage() {

        when(productService.searchProducts("phone", 0, 5))
                .thenReturn(productPage);

        Page<Product> result =
                productController.searchProducts("phone", 0, 5);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(productService).searchProducts("phone", 0, 5);
    }

    // ================= GET BY CATEGORY =================
    @Test
    void getByCategory_shouldReturnPage() {

        when(productService.getProductsByCategory(Category.ELECTRONICS, 0, 5))
                .thenReturn(productPage);

        Page<Product> result =
                productController.getByCategory(Category.ELECTRONICS, 0, 5);

        assertNotNull(result);
        assertEquals(1, result.getContent().size());

        verify(productService)
                .getProductsByCategory(Category.ELECTRONICS, 0, 5);
    }
}
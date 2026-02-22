package com.example.ecommerce.service;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class CartServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private ProductRepositary productRepository;

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartServiceImpl cartService;

    private Cart cart;
    private Product product;
    private CartItem cartItem;

    @BeforeEach
    void setUp() {
        product = new Product();
        product.setId(1L);
        product.setName("Test Product");
        product.setPrice(500.0);
        product.setStock(10);

        cartItem = new CartItem();
        cartItem.setProduct(product);
        cartItem.setQuantity(2);

        cart = new Cart();
        cart.setUserId(100L);
        cart.setCartItems(new ArrayList<>());
    }

    // =============================
    // TEST: addToCart - new cart and new item
    // =============================

    void testAddToCart_ExistingItem() {
        cart.getCartItems().add(cartItem);

        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.of(product));
        when(cartRepository.save(any(Cart.class))).thenAnswer(i -> i.getArgument(0));

        Cart result = cartService.addToCart(100L, 1L, 3);

        assertEquals(1, result.getCartItems().size());
        assertEquals(5, result.getCartItems().get(0).getQuantity()); // 2 + 3
        verify(cartRepository).save(cart);
    }

    // =============================
    // TEST: addToCart - product not found
    // =============================
    @Test
    void testAddToCart_ProductNotFound() {
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(cart));
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartService.addToCart(100L, 1L, 2));

        assertEquals("Product not found", exception.getMessage());
    }

    // =============================
    // TEST: getCartByUserId - success
    // =============================
    @Test
    void testGetCartByUserId_Success() {
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(cart));

        Cart result = cartService.getCartByUserId(100L);

        assertNotNull(result);
        assertEquals(100L, result.getUserId());
    }

    // =============================
    // TEST: getCartByUserId - cart not found
    // =============================
    @Test
    void testGetCartByUserId_NotFound() {
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartService.getCartByUserId(100L));

        assertEquals("Cart not found", exception.getMessage());
    }

    // =============================
    // TEST: removeFromCart - success
    // =============================
    @Test
    void testRemoveFromCart_Success() {
        CartItem anotherItem = new CartItem();
        Product anotherProduct = new Product();
        anotherProduct.setId(2L);
        anotherItem.setProduct(anotherProduct);
        anotherItem.setQuantity(1);

        cart.getCartItems().add(cartItem);
        cart.getCartItems().add(anotherItem);

        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(cart));
        when(cartRepository.save(cart)).thenReturn(cart);

        cartService.removeFromCart(100L, 1L);

        assertEquals(1, cart.getCartItems().size());
        assertEquals(2L, cart.getCartItems().get(0).getProduct().getId());

        verify(cartRepository).save(cart);
    }

    // =============================
    // TEST: removeFromCart - cart not found
    // =============================
    @Test
    void testRemoveFromCart_CartNotFound() {
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> cartService.removeFromCart(100L, 1L));

        assertEquals("Cart not found", exception.getMessage());
    }
}

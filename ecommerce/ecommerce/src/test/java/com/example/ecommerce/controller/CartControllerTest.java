package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartRequest;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.CartService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class CartControllerTest {

    @Mock
    private CartService cartService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private CartController cartController;

    private User user;
    private Cart cart;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        cart = new Cart();

        when(authentication.getPrincipal()).thenReturn(user);
    }

    // ================= ADD TO CART =================
    @Test
    void addToCart_shouldReturnCart() {

        CartRequest request = new CartRequest();
        request.setProductId(10L);

        when(cartService.addToCart(1L, 10L, 1)).thenReturn(cart);

        Cart result = cartController.addToCart(request, authentication);

        assertNotNull(result);
        assertEquals(cart, result);

        verify(cartService).addToCart(1L, 10L, 1);
    }

    // ================= GET CART =================
    @Test
    void getCart_shouldReturnCart() {

        when(cartService.getCartByUserId(1L)).thenReturn(cart);

        Cart result = cartController.getCart(authentication);

        assertNotNull(result);
        assertEquals(cart, result);

        verify(cartService).getCartByUserId(1L);
    }

    // ================= REMOVE FROM CART =================
    @Test
    void removeFromCart_shouldCallService() {

        cartController.removeFromCart(10L, authentication);

        verify(cartService).removeFromCart(1L, 10L);
    }

    // ================= UPDATE QUANTITY =================
    @Test
    void updateQuantity_shouldReturnUpdatedCart() {

        when(cartService.updateQuantity(1L, 10L, 1)).thenReturn(cart);

        Cart result = cartController.updateQuantity(10L, 1, authentication);

        assertNotNull(result);
        assertEquals(cart, result);

        verify(cartService).updateQuantity(1L, 10L, 1);
    }
}
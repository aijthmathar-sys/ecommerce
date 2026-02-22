package com.example.ecommerce.service;

import com.example.ecommerce.entity.Cart;

public interface CartService {

    Cart addToCart(Long userId, Long productId, int quantity);

    Cart getCartByUserId(Long userId);

    void removeFromCart(Long userId, Long productId);
  Cart  updateQuantity(Long userId, Long productId, int change);
}
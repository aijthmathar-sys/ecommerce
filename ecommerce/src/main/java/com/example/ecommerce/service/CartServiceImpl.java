package com.example.ecommerce.service;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class CartServiceImpl implements CartService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private ProductRepositary productRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Override
    public Cart addToCart(Long userId, Long productId, int quantity) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseGet(() -> {
                    Cart newCart = new Cart();
                    newCart.setUserId(userId);
                    return cartRepository.save(newCart);
                });

        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Product not found"));

        if (cart.getCartItems() != null) {
            Optional<CartItem> existingItem = cart.getCartItems()
                    .stream()
                    .filter(item -> item.getProduct().getId().equals(productId))
                    .findFirst();

            if (existingItem.isPresent()) {
                CartItem item = existingItem.get();
                item.setQuanity(item.getQuanity() + quantity);
                return cartRepository.save(cart);
            }
        }

        CartItem newItem = new CartItem();
        newItem.setCart(cart);
        newItem.setProduct(product);
        newItem.setQuanity(quantity);

        cart.getCartItems().add(newItem);
        cartRepository.save(cart);

        return cartRepository.findByUserId(userId).get();
    }

    @Override
    public Cart getCartByUserId(Long userId) {
        return cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));
    }

    @Override
    public void removeFromCart(Long userId, Long productId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        cart.getCartItems().removeIf(item ->
                item.getProduct().getId().equals(productId));

        cartRepository.save(cart);
    }
}
package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/cart")
@CrossOrigin("*")
public class CartController {

    @Autowired
    private CartService cartService;

    // ✅ Add Product to Cart
    @PostMapping("/add")
    public Cart addToCart(
            @RequestParam Long userId,
            @RequestParam Long productId,
            @RequestParam int quantity) {

        return cartService.addToCart(userId, productId, quantity);
    }

    // ✅ Get Cart by UserId
    @GetMapping("/{userId}")
    public Cart getCart(@PathVariable Long userId) {
        return cartService.getCartByUserId(userId);
    }

    // ✅ Remove Product from Cart
    @DeleteMapping("/remove")
    public void removeFromCart(
            @RequestParam Long userId,
            @RequestParam Long productId) {

        cartService.removeFromCart(userId, productId);
    }
}

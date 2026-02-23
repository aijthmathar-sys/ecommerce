package com.example.ecommerce.controller;

import com.example.ecommerce.dto.CartRequest;
import com.example.ecommerce.entity.Cart;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.CartService;

import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/cart")
@CrossOrigin(origins = "http://localhost:3000", allowCredentials = "true")
public class CartController {

    @Autowired
    private CartService cartService;

    @PostMapping("/add")
    public Cart addToCart(@Valid
            @RequestBody CartRequest request,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();

        return cartService.addToCart(
                user.getId(),
                request.getProductId(),
                1
        );
    }

    @GetMapping
    public Cart getCart(Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        return cartService.getCartByUserId(user.getId());
    }

    @DeleteMapping("/remove")
    public void removeFromCart(
            @RequestParam Long productId,
            Authentication authentication) {

        User user = (User) authentication.getPrincipal();
        cartService.removeFromCart(user.getId(), productId);
    }
    @PutMapping("/update")
public Cart updateQuantity(
        @RequestParam Long productId,
        @RequestParam int change,
        Authentication authentication) {

    User user = (User) authentication.getPrincipal();
    return cartService.updateQuantity(user.getId(), productId, change);
}
}
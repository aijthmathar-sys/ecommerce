package com.example.ecommerce.service;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderServiceImpl implements OrderService {

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OrderRepositary orderRepository;
    @Autowired
    private ProductRepositary productRepositary;

    @Override
    public Order placeOrder(Long userId) {

        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found"));

        if (cart.getCartItems().isEmpty()) {
            throw new RuntimeException("Cart is empty");
        }

        Order order = new Order();
        order.setUserId(userId);
        order.setOrderDate(LocalDateTime.now());
        order.setStatus("PLACED");

        double totalAmount = 0;

       for (CartItem cartItem : cart.getCartItems()) {

    Product product = cartItem.getProduct();

    // ✅ Check stock
    if (product.getStock() < cartItem.getQuanity()) {
        throw new RuntimeException("Insufficient stock for " + product.getName());
    }

    // ✅ Reduce stock
    product.setStock(product.getStock() - cartItem.getQuanity());
    productRepositary.save(product);

    OrderItem orderItem = new OrderItem();
    orderItem.setOrder(order);
    orderItem.setProduct(product);
    orderItem.setQuanity(cartItem.getQuanity());
    orderItem.setPrice(product.getPrice());

    order.getOrderItems().add(orderItem);

    totalAmount += product.getPrice() * cartItem.getQuanity();
}

        order.setTotalAmount(totalAmount);

        Order savedOrder = orderRepository.save(order);

        
        cart.getCartItems().clear();
        cartRepository.save(cart);

        return savedOrder;
        
    }

    @Override
    public List<Order> getOrdersByUserId(Long userId) {
        return orderRepository.findByUserId(userId);
    }
}
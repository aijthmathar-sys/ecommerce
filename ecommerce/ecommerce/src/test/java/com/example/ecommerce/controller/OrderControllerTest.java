package com.example.ecommerce.controller;

import com.example.ecommerce.entity.Order;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.service.OrderService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.security.core.Authentication;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class OrderControllerTest {

    @Mock
    private OrderService orderService;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private OrderController orderController;

    private User user;
    private Order order;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");

        order = new Order();

        when(authentication.getPrincipal()).thenReturn(user);
    }

    // ================= PLACE ORDER =================
    @Test
    void placeOrder_shouldReturnOrder() {

        when(orderService.placeOrder(1L)).thenReturn(order);

        Order result = orderController.placeOrder(authentication);

        assertNotNull(result);
        assertEquals(order, result);

        verify(orderService).placeOrder(1L);
    }

    // ================= GET MY ORDERS =================
    @Test
    void getMyOrders_shouldReturnOrderList() {

        List<Order> orderList = List.of(order);

        when(orderService.getOrdersByUserId(1L)).thenReturn(orderList);

        List<Order> result = orderController.getMyOrders(authentication);

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(order, result.get(0));

        verify(orderService).getOrdersByUserId(1L);
    }
}
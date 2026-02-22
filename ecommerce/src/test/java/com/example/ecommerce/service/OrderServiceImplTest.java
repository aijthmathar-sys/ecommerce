package com.example.ecommerce.service;

import com.example.ecommerce.entity.*;
import com.example.ecommerce.repository.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class OrderServiceImplTest {

    @Mock
    private CartRepository cartRepository;

    @Mock
    private OrderRepositary orderRepository;

    @Mock
    private ProductRepositary productRepositary;

    @InjectMocks
    private OrderServiceImpl orderService;

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
        cart.setId(1L);
        cart.setUserId(100L);
        cart.setCartItems(new ArrayList<>());
        cart.getCartItems().add(cartItem);
    }

    // =============================
    // TEST: placeOrder SUCCESS
    // =============================
    @Test
    void testPlaceOrder_Success() {

        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(cart));
        when(productRepositary.save(any(Product.class))).thenReturn(product);
        when(orderRepository.save(any(Order.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(cartRepository.save(any(Cart.class))).thenReturn(cart);

        Order savedOrder = orderService.placeOrder(100L);

        assertNotNull(savedOrder);
        assertEquals(100L, savedOrder.getUserId());
        assertEquals(1, savedOrder.getOrderItems().size());
        assertEquals(500.0 * 2, savedOrder.getTotalAmount());

        // Stock should be reduced
        assertEquals(8, product.getStock());

        verify(productRepositary, times(1)).save(product);
        verify(orderRepository, times(1)).save(savedOrder);
        verify(cartRepository, times(1)).save(cart);
    }

    // =============================
    // TEST: placeOrder CART NOT FOUND
    // =============================
    @Test
    void testPlaceOrder_CartNotFound() {
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(100L));

        assertEquals("Cart not found", exception.getMessage());
    }

    // =============================
    // TEST: placeOrder CART EMPTY
    // =============================
    @Test
    void testPlaceOrder_CartEmpty() {
        cart.setCartItems(new ArrayList<>());
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(cart));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(100L));

        assertEquals("Cart is empty", exception.getMessage());
    }

    // =============================
    // TEST: placeOrder INSUFFICIENT STOCK
    // =============================
    @Test
    void testPlaceOrder_InsufficientStock() {
        product.setStock(1); // less than cart quantity
        when(cartRepository.findByUserId(100L)).thenReturn(Optional.of(cart));

        RuntimeException exception = assertThrows(RuntimeException.class,
                () -> orderService.placeOrder(100L));

        assertTrue(exception.getMessage().contains("Insufficient stock"));
    }

    // =============================
    // TEST: getOrdersByUserId
    // =============================
    @Test
    void testGetOrdersByUserId() {
        Order order1 = new Order();
        order1.setId(1L);
        order1.setUserId(100L);

        Order order2 = new Order();
        order2.setId(2L);
        order2.setUserId(100L);

        List<Order> orders = List.of(order1, order2);

        when(orderRepository.findByUserId(100L)).thenReturn(orders);

        List<Order> result = orderService.getOrdersByUserId(100L);

        assertEquals(2, result.size());
        verify(orderRepository, times(1)).findByUserId(100L);
    }
}


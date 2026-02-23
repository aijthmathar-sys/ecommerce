package com.example.ecommerce.controller;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class AdminControllerTest {

    private final AdminController adminController = new AdminController();

    @Test
    void dashboard_shouldReturnWelcomeMessage() {

        String result = adminController.dashboard();

        assertNotNull(result);
        assertEquals("Welcome Admin 🔥", result);
    }
}

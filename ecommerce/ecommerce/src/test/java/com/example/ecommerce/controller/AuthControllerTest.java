package com.example.ecommerce.controller;

import com.example.ecommerce.dto.ForgotPasswordRequest;
import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.dto.ResetPasswordRequest;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.security.JwtUtil;
import com.example.ecommerce.service.UserService;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.Authentication;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    @Mock
    private UserService userService;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private Authentication authentication;

    @InjectMocks
    private AuthController authController;

    private User user;

    @BeforeEach
    void setUp() {
        user = new User();
        user.setId(1L);
        user.setEmail("test@example.com");
        user.setRole(Role.CUSTOMER); // ✅ Correct enum
    }

    // ================= REGISTER =================
    @Test
    void register_shouldReturnOk() {
        when(userService.register(any(User.class))).thenReturn(user);

        ResponseEntity<?> response = authController.register(user);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(user, response.getBody());
        verify(userService).register(user);
    }

    // ================= LOGIN =================
    @Test
    void login_shouldReturnJwtCookieAndRole() {

        LoginRequest request = new LoginRequest();
        request.setEmail("test@example.com");
        request.setPassword("password");

        when(userService.login(request)).thenReturn(user);
        when(jwtUtil.generateToken(anyString(), anyString()))
                .thenReturn("mock-jwt-token");

        MockHttpServletResponse servletResponse = new MockHttpServletResponse();

        ResponseEntity<?> response =
                authController.login(request, servletResponse);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, String> body = (Map<String, String>) response.getBody();

        assertEquals("Login Successful", body.get("message"));
        assertEquals("CUSTOMER", body.get("role"));

        String cookieHeader = servletResponse.getHeader("Set-Cookie");
        assertTrue(cookieHeader.contains("jwt=mock-jwt-token"));

        verify(userService).login(request);
        verify(jwtUtil).generateToken("test@example.com", "CUSTOMER");
    }

    // ================= LOGOUT =================
    @Test
    void logout_shouldClearCookie() {

        MockHttpServletResponse response = new MockHttpServletResponse();

        ResponseEntity<?> result = authController.logout(response);

        assertEquals(HttpStatus.OK, result.getStatusCode());
        assertEquals("Logged out", result.getBody());

        assertEquals(1, response.getCookies().length);
        assertEquals("jwt", response.getCookies()[0].getName());
        assertEquals(0, response.getCookies()[0].getMaxAge());
    }

    // ================= GET CURRENT USER =================
    @Test
    void getCurrentUser_shouldReturnUserDetails() {

        when(authentication.getPrincipal()).thenReturn(user);

        ResponseEntity<?> response =
                authController.getCurrentUser(authentication);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        Map<String, Object> body =
                (Map<String, Object>) response.getBody();

        assertEquals(1L, body.get("id"));
        assertEquals("test@example.com", body.get("email"));
        assertEquals("CUSTOMER", body.get("role"));
    }

    @Test
    void getCurrentUser_shouldReturnUnauthorized_whenNullAuth() {

        ResponseEntity<?> response =
                authController.getCurrentUser(null);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
    }

    // ================= FORGOT PASSWORD =================
    @Test
    void forgotPassword_shouldSendOtp() {

        ForgotPasswordRequest request = new ForgotPasswordRequest();
        request.setEmail("test@example.com");

        ResponseEntity<?> response =
                authController.forgotPassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("OTP sent successfully", response.getBody());

        verify(userService).sendOtp("test@example.com");
    }

    // ================= RESET PASSWORD =================
    @Test
    void resetPassword_shouldResetPassword() {

        ResetPasswordRequest request = new ResetPasswordRequest();
        request.setEmail("test@example.com");
        request.setOtp("123456");
        request.setNewPassword("newPassword");

        ResponseEntity<?> response =
                authController.resetPassword(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Password reset successful", response.getBody());

        verify(userService)
                .resetPassword("test@example.com", "123456", "newPassword");
    }
}
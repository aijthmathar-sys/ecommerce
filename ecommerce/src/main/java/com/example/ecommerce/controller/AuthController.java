package com.example.ecommerce.controller;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.security.JwtUtil;
import com.example.ecommerce.service.UserService;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    @Autowired
    private UserService userService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody User user) {
        return ResponseEntity.ok(userService.register(user));
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request,
                                   HttpServletResponse response) {

        User user = userService.login(request);

        String token = jwtUtil.generateToken(
                user.getEmail(),
                user.getRole().name()
        );

        ResponseCookie cookie = ResponseCookie.from("jwt", token)
        .httpOnly(true)
        .secure(false) // true only in HTTPS
        .path("/")
        .maxAge(60 * 60)
        .sameSite("Lax")
        .build();

response.setHeader(HttpHeaders.SET_COOKIE, cookie.toString());

Map<String, String> responseBody = new HashMap<>();
responseBody.put("message", "Login Successful");
responseBody.put("role", user.getRole().name());

return ResponseEntity.ok(responseBody);

        

        
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {

        Cookie cookie = new Cookie("jwt", null);
        cookie.setHttpOnly(true);
        cookie.setPath("/");
        cookie.setMaxAge(0);

        response.addCookie(cookie);

        return ResponseEntity.ok("Logged out");
    }
    @GetMapping("/me")
public ResponseEntity<?> getCurrentUser(Authentication authentication) {

    if (authentication == null) {
        return ResponseEntity.status(401).body("Not authenticated");
    }

    Object principal = authentication.getPrincipal();

    if (!(principal instanceof User)) {
        return ResponseEntity.status(401).body("Invalid user");
    }

    User user = (User) principal;

    return ResponseEntity.ok(Map.of(
            "id", user.getId(),
            "email", user.getEmail(),
            "role", user.getRole().name()
    ));
}
@PostMapping("/forgot-password")
public ResponseEntity<?> forgotPassword(@RequestBody Map<String, String> body) {

    userService.sendOtp(body.get("email"));
    return ResponseEntity.ok("OTP sent successfully");
}
@PostMapping("/reset-password")
public ResponseEntity<?> resetPassword(@RequestBody Map<String, String> body) {

    userService.resetPassword(
            body.get("email"),
            body.get("otp"),
            body.get("newPassword")
    );

    return ResponseEntity.ok("Password reset successful");
}

}
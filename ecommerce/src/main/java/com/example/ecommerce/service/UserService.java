package com.example.ecommerce.service;

import java.time.LocalDateTime;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.example.ecommerce.dto.LoginRequest;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.entity.User;
import com.example.ecommerce.repository.UserRepository;
import com.example.ecommerce.security.JwtUtil;



@Service
public class UserService {
    @Autowired 
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private JwtUtil jwtUtil;
    @Autowired
    private EmailService emailService;

    public User register(User user){
        if(userRepository.existsByEmail(user.getEmail()))
        {
            throw new RuntimeException("Email already exists");
        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
            user.setRole(Role.CUSTOMER);
            return userRepository.save(user);
    }
   public User login(LoginRequest request) {

    User user = userRepository.findByEmail(request.getEmail())
            .orElseThrow(() ->
                    new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found")
            );

    if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid password");
    }

    return user;
}
public void sendOtp(String email) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    String otp = String.valueOf((int)(Math.random() * 900000) + 100000);

    user.setOtp(otp);
    user.setOtpExpiry(LocalDateTime.now().plusMinutes(5));

    userRepository.save(user);

    emailService.sendOtp(email, otp);
}
public void resetPassword(String email, String otp, String newPassword) {

    User user = userRepository.findByEmail(email)
            .orElseThrow(() -> new RuntimeException("User not found"));

    if (user.getOtp() == null ||
        !user.getOtp().equals(otp) ||
        user.getOtpExpiry().isBefore(LocalDateTime.now())) {

        throw new RuntimeException("Invalid or Expired OTP");
    }

    user.setPassword(passwordEncoder.encode(newPassword));

    user.setOtp(null);
    user.setOtpExpiry(null);

    userRepository.save(user);
}
}

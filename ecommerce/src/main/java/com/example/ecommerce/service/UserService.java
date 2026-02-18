package com.example.ecommerce.service;

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
}

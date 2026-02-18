 package com.example.ecommerce;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.example.ecommerce.entity.User;
import com.example.ecommerce.entity.Role;
import com.example.ecommerce.repository.UserRepository;

@SpringBootApplication
public class EcommerceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EcommerceApplication.class, args);
    }

    @Bean
    CommandLineRunner createAdmin(UserRepository userRepository,
                                  PasswordEncoder passwordEncoder) {
        return args -> {

            if (!userRepository.existsByEmail("admin@gmail.com")) {

                User admin = new User();
                admin.setName("Admin");
                admin.setEmail("admin@gmail.com");
                admin.setPassword(passwordEncoder.encode("Admin@123"));
                admin.setRole(Role.ADMIN);

                userRepository.save(admin);

                System.out.println(" Default Admin Created Successfully!");
            }
        };
    }
}

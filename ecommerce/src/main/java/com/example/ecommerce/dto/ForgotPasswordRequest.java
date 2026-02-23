package com.example.ecommerce.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForgotPasswordRequest {

    @Email(message = "Invalid email format")
    @NotBlank(message = "Email is required")
    private String email;
}
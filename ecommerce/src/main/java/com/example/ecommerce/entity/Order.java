package com.example.ecommerce.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="orders")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class Order {
   @Id
   @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    private Long UserId;
    private Double totalAmount;
    private LocalDateTime orderDate;
    private String status;
    @OneToMany(mappedBy = "order",cascade = CascadeType.ALL,orphanRemoval = true)
    @JsonManagedReference
    private List<OrderItem> orderItems=new ArrayList<>();
}

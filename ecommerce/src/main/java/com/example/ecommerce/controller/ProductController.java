package com.example.ecommerce.controller;

import java.util.List;
import org.springframework.web.bind.annotation.*;
import lombok.RequiredArgsConstructor;
import com.example.ecommerce.entity.Product;
import com.example.ecommerce.service.ProductService;
import org.springframework.data.domain.Page;

@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    @PostMapping
    public Product addProduct(@RequestBody Product product) {
        return productService.addProduct(product);
    }

   @GetMapping
public Page<Product> getAllProducts(
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

    page = Math.max(page, 0);
    size = Math.min(Math.max(size, 1), 20);

    return productService.getAllProducts(page, size);
}

    @GetMapping("/{id}")
    public Product getProductById(@PathVariable Long id) {
        return productService.getProductById(id);
    }

    @PutMapping("/{id}")
    public Product updateProduct(@PathVariable Long id,
                                 @RequestBody Product product) {
        return productService.updateProduct(id, product);
    }

    @DeleteMapping("/{id}")
    public void deleteProduct(@PathVariable Long id) {
        productService.deleteProduct(id);
    }
    @GetMapping("/search")
public Page<Product> searchProducts(
        @RequestParam String keyword,
        @RequestParam(defaultValue = "0") int page,
        @RequestParam(defaultValue = "5") int size) {

    page = Math.max(page, 0);
    size = Math.min(Math.max(size, 1), 20);

    return productService.searchProducts(keyword, page, size);
}
}

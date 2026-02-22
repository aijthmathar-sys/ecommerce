package com.example.ecommerce.service;

import java.util.List;
import com.example.ecommerce.entity.Category;

import javax.management.RuntimeErrorException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.repository.ProductRepositary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Service
public class ProductService {

    @Autowired
    private ProductRepositary productRepositary;

    public Product addProduct(Product product)
    {
        return productRepositary.save(product);
    }

   public Page<Product> getAllProducts(int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

    return productRepositary.findAll(pageable);
}

    public Product getProductById(Long id){
        return productRepositary.findById(id).orElseThrow(()-> new RuntimeException("product not found"));
    }
   public Product updateProduct(Long id, Product updateProduct) {

    Product product = productRepositary.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    product.setName(updateProduct.getName());
    product.setDescription(updateProduct.getDescription());
    product.setPrice(updateProduct.getPrice());
    product.setStock(updateProduct.getStock());
    product.setImageurl(updateProduct.getImageurl());

    return productRepositary.save(product);
    }
public  Page<Product> searchProducts(String keyword,int page,int size){
        Pageable pageable=PageRequest.of(page,size,Sort.by("id").descending());
        return productRepositary.findByNameContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
            keyword, keyword, pageable
        );
    }
    public void deleteProduct(Long id) {

    Product product = productRepositary.findById(id)
            .orElseThrow(() -> new RuntimeException("Product not found"));

    productRepositary.delete(product);
}
public Page<Product> getProductsByCategory(Category category, int page, int size) {

    Pageable pageable = PageRequest.of(page, size, Sort.by("id").descending());

    return productRepositary.findByCategory(category, pageable);
}
}

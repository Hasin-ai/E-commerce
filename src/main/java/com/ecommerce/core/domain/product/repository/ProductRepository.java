package com.ecommerce.core.domain.product.repository;

import com.ecommerce.core.domain.product.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Optional<Product> findById(Long id);
    List<Product> findAll();
    Product save(Product product);
    void delete(Product product);
    void deleteById(Long id);
    List<Product> findByCategory(Long categoryId);
    List<Product> findByNameContaining(String name);
    List<Product> findFeaturedProducts();
    boolean existsById(Long id);
    
    // Additional methods for extended functionality
    Optional<Product> findBySlug(String slug);
    org.springframework.data.domain.Page<Product> findByCategoryId(Long categoryId, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<Product> findByNameContainingIgnoreCase(String name, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<Product> findByFeatured(boolean featured, org.springframework.data.domain.Pageable pageable);
    org.springframework.data.domain.Page<Product> findByActive(boolean active, org.springframework.data.domain.Pageable pageable);
}
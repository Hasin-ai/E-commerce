package com.ecommerce.core.domain.product.repository;

import com.ecommerce.core.domain.product.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    Product save(Product product);
    Optional<Product> findById(Long id);
    Optional<Product> findBySku(String sku);
    List<Product> findByCategoryId(Long categoryId);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
    Page<Product> findByIsActiveTrue(Pageable pageable);
    boolean existsBySku(String sku);
    void delete(Product product);
}

package com.ecommerce.core.domain.product.repository;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.valueobject.SKU;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {

    Product save(Product product);

    Optional<Product> findById(Long id);

    Optional<Product> findBySlug(String slug);

    Optional<Product> findBySku(SKU sku);

    List<Product> findAll();

    List<Product> findByCategory(Long categoryId);

    List<Product> findActiveProducts();

    List<Product> findFeaturedProducts();

    List<Product> findByNameContaining(String name);

    boolean existsBySku(SKU sku);

    boolean existsBySlug(String slug);

    boolean existsById(Long id);

    void deleteById(Long id);

    long count();

    // Pagination support
    List<Product> findAll(int page, int size);

    List<Product> findActiveProducts(int page, int size);
}

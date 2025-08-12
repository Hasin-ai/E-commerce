package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.jpa.entity.ProductJpaEntity;
import com.ecommerce.adapter.persistence.jpa.repository.ProductJpaRepository;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.product.valueobject.Price;
import org.springframework.context.annotation.Primary;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
@Primary
public class ProductJpaRepositoryAdapter implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;

    public ProductJpaRepositoryAdapter(ProductJpaRepository productJpaRepository) {
        this.productJpaRepository = productJpaRepository;
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id).map(this::mapToDomain);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = mapToEntity(product);
        ProductJpaEntity savedEntity = productJpaRepository.save(entity);
        return mapToDomain(savedEntity);
    }

    @Override
    public void delete(Product product) {
        if (product.getId() != null) {
            productJpaRepository.deleteById(product.getId());
        }
    }

    @Override
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productJpaRepository.findByCategoryId(categoryId).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        return productJpaRepository.findByNameContainingIgnoreCase(name).stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findFeaturedProducts() {
        return productJpaRepository.findByIsFeaturedTrue().stream()
                .map(this::mapToDomain)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return productJpaRepository.existsById(id);
    }

    @Override
    public Optional<Product> findBySlug(String slug) {
        return productJpaRepository.findBySlug(slug).map(this::mapToDomain);
    }

    @Override
    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        List<Product> categoryProducts = findByCategory(categoryId);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), categoryProducts.size());
        List<Product> pageContent = categoryProducts.subList(start, end);
        return new PageImpl<>(pageContent, pageable, categoryProducts.size());
    }

    @Override
    public Page<Product> findByActive(boolean active, Pageable pageable) {
        Page<ProductJpaEntity> entityPage = productJpaRepository.findByIsActiveTrue(pageable);
        return entityPage.map(this::mapToDomain);
    }

    @Override
    public Page<Product> findByFeatured(boolean featured, Pageable pageable) {
        Page<ProductJpaEntity> entityPage = productJpaRepository.findByIsFeaturedTrue(pageable);
        return entityPage.map(this::mapToDomain);
    }

    @Override
    public Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        List<Product> matchingProducts = findByNameContaining(name);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), matchingProducts.size());
        List<Product> pageContent = matchingProducts.subList(start, end);
        return new PageImpl<>(pageContent, pageable, matchingProducts.size());
    }

    private Product mapToDomain(ProductJpaEntity entity) {
        Product product = new Product();
        product.setId(entity.getId());
        product.setName(entity.getName());
        product.setSlug(entity.getSlug());
        product.setDescription(entity.getDescription());
        product.setSku(entity.getSku());
        
        // Create Price value object from entity
        if (entity.getBasePrice() != null && entity.getCurrency() != null) {
            product.setPrice(new Price(entity.getBasePrice(), entity.getCurrency()));
        }
        
        product.setCategoryId(entity.getCategoryId());
        product.setStockQuantity(entity.getStockQuantity() != null ? entity.getStockQuantity() : 0); // FIX: Add missing stockQuantity mapping
        product.setActive(entity.getIsActive() != null ? entity.getIsActive() : true);
        product.setFeatured(entity.getIsFeatured() != null ? entity.getIsFeatured() : false);
        product.setCreatedAt(entity.getCreatedAt());
        product.setUpdatedAt(entity.getUpdatedAt());
        return product;
    }

    private ProductJpaEntity mapToEntity(Product product) {
        ProductJpaEntity entity = new ProductJpaEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setSlug(product.getSlug());
        entity.setDescription(product.getDescription());
        entity.setSku(product.getSku());
        
        // Extract amount and currency from Price value object
        if (product.getPrice() != null) {
            entity.setBasePrice(product.getPrice().getAmount());
            entity.setCurrency(product.getPrice().getCurrency());
        }
        
        entity.setCategoryId(product.getCategoryId());
        entity.setStockQuantity(product.getStockQuantity()); // FIX: Add missing stockQuantity mapping
        entity.setIsActive(product.isActive());
        entity.setIsFeatured(product.isFeatured());
        entity.setCreatedAt(product.getCreatedAt() != null ? product.getCreatedAt() : LocalDateTime.now());
        entity.setUpdatedAt(product.getUpdatedAt() != null ? product.getUpdatedAt() : LocalDateTime.now());
        return entity;
    }
}

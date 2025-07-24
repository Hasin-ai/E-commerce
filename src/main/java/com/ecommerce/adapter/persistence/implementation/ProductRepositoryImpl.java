package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.adapter.persistence.jpa.repository.ProductJpaRepository;
import com.ecommerce.adapter.persistence.jpa.entity.ProductJpaEntity;
import com.ecommerce.adapter.persistence.mapper.ProductMapper;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository jpaRepository;
    private final ProductMapper mapper;

    public ProductRepositoryImpl(ProductJpaRepository jpaRepository, ProductMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity entity = mapper.toEntity(product);
        ProductJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return jpaRepository.findById(id)
            .map(mapper::toDomain);
    }

    @Override
    public Optional<Product> findBySku(String sku) {
        return jpaRepository.findBySku(sku)
            .map(mapper::toDomain);
    }

    @Override
    public List<Product> findByCategoryId(Long categoryId) {
        return jpaRepository.findByCategoryId(categoryId)
            .stream()
            .map(mapper::toDomain)
            .collect(Collectors.toList());
    }

    @Override
    public Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable) {
        // Use the existing method from ProductJpaRepository
        List<ProductJpaEntity> products = jpaRepository.findByNameContainingIgnoreCase(name);
        // Convert to Page manually since the existing method returns List
        return new org.springframework.data.domain.PageImpl<>(
            products.stream().map(mapper::toDomain).collect(Collectors.toList()),
            pageable,
            products.size()
        );
    }

    @Override
    public Page<Product> findByIsActiveTrue(Pageable pageable) {
        return jpaRepository.findByIsActiveTrue(pageable)
            .map(mapper::toDomain);
    }

    @Override
    public boolean existsBySku(String sku) {
        return jpaRepository.existsBySku(sku);
    }

    @Override
    public void delete(Product product) {
        ProductJpaEntity entity = mapper.toEntity(product);
        jpaRepository.delete(entity);
    }

    // Additional methods for our use cases
    public Optional<Product> findBySlug(String slug) {
        return jpaRepository.findBySlug(slug)
            .map(mapper::toDomain);
    }

    public Page<Product> findByActive(boolean active, Pageable pageable) {
        if (active) {
            return jpaRepository.findByIsActiveTrue(pageable)
                .map(mapper::toDomain);
        } else {
            // For inactive products, we'd need a custom query or return empty page
            return Page.empty(pageable);
        }
    }

    public Page<Product> findByFeatured(boolean featured, Pageable pageable) {
        if (featured) {
            return jpaRepository.findByIsFeaturedTrue(pageable)
                .map(mapper::toDomain);
        } else {
            // For non-featured products, we'd need a custom query or return empty page
            return Page.empty(pageable);
        }
    }

    public Page<Product> findByCategoryId(Long categoryId, Pageable pageable) {
        List<ProductJpaEntity> products = jpaRepository.findByCategoryId(categoryId);
        // Convert to Page manually since the existing method returns List
        return new org.springframework.data.domain.PageImpl<>(
            products.stream().map(mapper::toDomain).collect(Collectors.toList()),
            pageable,
            products.size()
        );
    }
}
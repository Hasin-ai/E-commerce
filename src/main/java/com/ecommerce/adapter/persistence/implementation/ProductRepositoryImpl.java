package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.adapter.persistence.jpa.entity.ProductJpaEntity;
import com.ecommerce.adapter.persistence.jpa.repository.ProductJpaRepository;
import com.ecommerce.adapter.persistence.mapper.ProductEntityMapper;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.product.valueobject.SKU;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class ProductRepositoryImpl implements ProductRepository {

    private final ProductJpaRepository productJpaRepository;
    private final ProductEntityMapper productEntityMapper;

    @Autowired
    public ProductRepositoryImpl(ProductJpaRepository productJpaRepository,
                                 ProductEntityMapper productEntityMapper) {
        this.productJpaRepository = productJpaRepository;
        this.productEntityMapper = productEntityMapper;
    }

    @Override
    public Product save(Product product) {
        ProductJpaEntity jpaEntity = productEntityMapper.toJpaEntity(product);
        ProductJpaEntity savedEntity = productJpaRepository.save(jpaEntity);
        return productEntityMapper.toDomainEntity(savedEntity);
    }

    @Override
    public Optional<Product> findById(Long id) {
        return productJpaRepository.findById(id)
                .map(productEntityMapper::toDomainEntity);
    }

    @Override
    public Optional<Product> findBySlug(String slug) {
        return productJpaRepository.findBySlug(slug)
                .map(productEntityMapper::toDomainEntity);
    }

    @Override
    public Optional<Product> findBySku(SKU sku) {
        return productJpaRepository.findBySku(sku.getValue())
                .map(productEntityMapper::toDomainEntity);
    }

    @Override
    public List<Product> findAll() {
        return productJpaRepository.findAll()
                .stream()
                .map(productEntityMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        return productJpaRepository.findByCategoryId(categoryId)
                .stream()
                .map(productEntityMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findActiveProducts() {
        return productJpaRepository.findByIsActiveTrue()
                .stream()
                .map(productEntityMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findFeaturedProducts() {
        return productJpaRepository.findByIsFeaturedTrue()
                .stream()
                .map(productEntityMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        return productJpaRepository.findByNameContainingIgnoreCase(name)
                .stream()
                .map(productEntityMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public boolean existsBySku(SKU sku) {
        return productJpaRepository.existsBySku(sku.getValue());
    }

    @Override
    public boolean existsBySlug(String slug) {
        return productJpaRepository.existsBySlug(slug);
    }

    @Override
    public boolean existsById(Long id) {
        return productJpaRepository.existsById(id);
    }

    @Override
    public void deleteById(Long id) {
        productJpaRepository.deleteById(id);
    }

    @Override
    public long count() {
        return productJpaRepository.count();
    }

    @Override
    public List<Product> findAll(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return productJpaRepository.findAll(pageable)
                .stream()
                .map(productEntityMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> findActiveProducts(int page, int size) {
        PageRequest pageable = PageRequest.of(page, size);
        return productJpaRepository.findByIsActiveTrue(pageable)
                .stream()
                .map(productEntityMapper::toDomainEntity)
                .collect(Collectors.toList());
    }

    @Override
    public List<Product> getRecommendations(String userId) {
        // This is a simplified implementation. In a real-world scenario,
        // we would use a recommendation engine or algorithm based on user preferences.
        // For now, we'll just return featured products as recommendations.
        return findFeaturedProducts();
    }
}
package com.ecommerce.adapter.persistence.implementation;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

@Repository
public class SimpleProductRepositoryImpl implements ProductRepository {

    private final Map<Long, Product> products = new ConcurrentHashMap<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public SimpleProductRepositoryImpl() {
        initializeSampleProducts();
    }

    @Override
    public Optional<Product> findById(Long id) {
        return Optional.ofNullable(products.get(id));
    }

    @Override
    public List<Product> findAll() {
        return new ArrayList<>(products.values());
    }

    @Override
    public Product save(Product product) {
        if (product.getId() == null) {
            product.setId(idGenerator.getAndIncrement());
            product.setCreatedAt(LocalDateTime.now());
        }
        product.setUpdatedAt(LocalDateTime.now());
        products.put(product.getId(), product);
        return product;
    }

    @Override
    public void delete(Product product) {
        products.remove(product.getId());
    }

    @Override
    public void deleteById(Long id) {
        products.remove(id);
    }

    @Override
    public List<Product> findByCategory(Long categoryId) {
        return products.values().stream()
            .filter(product -> Objects.equals(product.getCategoryId(), categoryId))
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> findByNameContaining(String name) {
        return products.values().stream()
            .filter(product -> product.getName().toLowerCase().contains(name.toLowerCase()))
            .collect(Collectors.toList());
    }

    @Override
    public List<Product> findFeaturedProducts() {
        return products.values().stream()
            .filter(Product::isFeatured)
            .collect(Collectors.toList());
    }

    @Override
    public boolean existsById(Long id) {
        return products.containsKey(id);
    }

    private void initializeSampleProducts() {
        // Gaming Laptop
        Product laptop = new Product();
        laptop.setId(1L);
        laptop.setName("Gaming Laptop");
        laptop.setSlug("gaming-laptop");
        laptop.setDescription("High-performance gaming laptop with RTX graphics");
        laptop.setSku("LAPTOP-001");
        laptop.setPrice(BigDecimal.valueOf(1299.99));
        laptop.setCategoryId(1L);
        laptop.setImageUrl("/images/gaming-laptop.jpg");
        laptop.setStockQuantity(10);
        laptop.setCurrency("USD");
        laptop.setActive(true);
        laptop.setFeatured(true);
        laptop.setCreatedAt(LocalDateTime.now());
        laptop.setUpdatedAt(LocalDateTime.now());
        products.put(laptop.getId(), laptop);

        // Gaming Mouse
        Product mouse = new Product();
        mouse.setId(2L);
        mouse.setName("Gaming Mouse");
        mouse.setSlug("gaming-mouse");
        mouse.setDescription("RGB gaming mouse with high DPI");
        mouse.setSku("MOUSE-001");
        mouse.setPrice(BigDecimal.valueOf(79.99));
        mouse.setCategoryId(1L);
        mouse.setImageUrl("/images/gaming-mouse.jpg");
        mouse.setStockQuantity(25);
        mouse.setCurrency("USD");
        mouse.setActive(true);
        mouse.setFeatured(false);
        mouse.setCreatedAt(LocalDateTime.now());
        mouse.setUpdatedAt(LocalDateTime.now());
        products.put(mouse.getId(), mouse);

        // Mechanical Keyboard
        Product keyboard = new Product();
        keyboard.setId(3L);
        keyboard.setName("Mechanical Keyboard");
        keyboard.setSlug("mechanical-keyboard");
        keyboard.setDescription("RGB mechanical keyboard with blue switches");
        keyboard.setSku("KEYBOARD-001");
        keyboard.setPrice(BigDecimal.valueOf(149.99));
        keyboard.setCategoryId(1L);
        keyboard.setImageUrl("/images/mechanical-keyboard.jpg");
        keyboard.setStockQuantity(15);
        keyboard.setCurrency("USD");
        keyboard.setActive(true);
        keyboard.setFeatured(true);
        keyboard.setCreatedAt(LocalDateTime.now());
        keyboard.setUpdatedAt(LocalDateTime.now());
        products.put(keyboard.getId(), keyboard);
    }

    @Override
    public Optional<Product> findBySlug(String slug) {
        return products.values().stream()
            .filter(product -> Objects.equals(product.getSlug(), slug))
            .findFirst();
    }

    public org.springframework.data.domain.Page<Product> findByCategoryId(Long categoryId, org.springframework.data.domain.Pageable pageable) {
        List<Product> categoryProducts = findByCategory(categoryId);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), categoryProducts.size());
        List<Product> pageContent = categoryProducts.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, categoryProducts.size());
    }

    @Override
    public org.springframework.data.domain.Page<Product> findByActive(boolean active, org.springframework.data.domain.Pageable pageable) {
        List<Product> activeProducts = products.values().stream()
            .filter(product -> product.isActive() == active)
            .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), activeProducts.size());
        List<Product> pageContent = activeProducts.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, activeProducts.size());
    }

    @Override
    public org.springframework.data.domain.Page<Product> findByFeatured(boolean featured, org.springframework.data.domain.Pageable pageable) {
        List<Product> featuredProducts = products.values().stream()
            .filter(product -> product.isFeatured() == featured)
            .collect(Collectors.toList());
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), featuredProducts.size());
        List<Product> pageContent = featuredProducts.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, featuredProducts.size());
    }

    public org.springframework.data.domain.Page<Product> findByNameContainingIgnoreCase(String name, org.springframework.data.domain.Pageable pageable) {
        List<Product> matchingProducts = findByNameContaining(name);
        int start = (int) pageable.getOffset();
        int end = Math.min(start + pageable.getPageSize(), matchingProducts.size());
        List<Product> pageContent = matchingProducts.subList(start, end);
        return new org.springframework.data.domain.PageImpl<>(pageContent, pageable, matchingProducts.size());
    }
}
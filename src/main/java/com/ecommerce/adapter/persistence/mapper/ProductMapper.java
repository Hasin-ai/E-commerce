package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.adapter.persistence.jpa.entity.ProductJpaEntity;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductMapper {

    public ProductJpaEntity toEntity(Product product) {
        if (product == null) {
            return null;
        }

        ProductJpaEntity entity = new ProductJpaEntity();
        entity.setId(product.getId());
        entity.setName(product.getName());
        entity.setSlug(generateSlug(product.getName()));
        entity.setDescription(product.getDescription());
        entity.setSku(product.getSku());
        entity.setBasePrice(product.getPrice().getAmount());
        entity.setCurrency(product.getPrice().getCurrency());
        entity.setSalePrice(null); // Set to null for now, can be enhanced later
        entity.setIsActive(product.isActive());
        entity.setIsFeatured(false); // Default to false, can be enhanced later
        entity.setCreatedAt(product.getCreatedAt());
        entity.setUpdatedAt(product.getUpdatedAt());

        return entity;
    }

    public Product toDomain(ProductJpaEntity entity) {
        if (entity == null) {
            return null;
        }

        Price price = new Price(entity.getBasePrice(), entity.getCurrency());
        
        // For now, we'll use a default categoryId and stockQuantity since they're not in ProductJpaEntity
        // This should be enhanced when the entity structure is updated
        Product product = new Product(
            entity.getName(),
            entity.getDescription(),
            entity.getSku(),
            price,
            null, // categoryId - not available in current ProductJpaEntity
            0     // stockQuantity - not available in current ProductJpaEntity
        );

        // Use reflection to set private fields for persistence
        try {
            java.lang.reflect.Field idField = Product.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(product, entity.getId());

            java.lang.reflect.Field isActiveField = Product.class.getDeclaredField("isActive");
            isActiveField.setAccessible(true);
            isActiveField.set(product, entity.getIsActive());

            java.lang.reflect.Field createdAtField = Product.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(product, entity.getCreatedAt());

            java.lang.reflect.Field updatedAtField = Product.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(product, entity.getUpdatedAt());

        } catch (Exception e) {
            throw new RuntimeException("Error mapping product entity to domain", e);
        }

        return product;
    }

    private String generateSlug(String name) {
        if (name == null) return null;
        return name.toLowerCase()
            .replaceAll("[^a-z0-9\\s-]", "")
            .replaceAll("\\s+", "-")
            .replaceAll("-+", "-")
            .replaceAll("^-|-$", "");
    }
}
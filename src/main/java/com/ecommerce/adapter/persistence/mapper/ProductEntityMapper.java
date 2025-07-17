package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.adapter.persistence.jpa.entity.ProductJpaEntity;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.valueobject.Price;
import com.ecommerce.core.domain.product.valueobject.SKU;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;

@Component
public class ProductEntityMapper {

    public ProductJpaEntity toJpaEntity(Product product) {
        ProductJpaEntity jpaEntity = new ProductJpaEntity();

        jpaEntity.setId(product.getId());
        jpaEntity.setName(product.getName());
        jpaEntity.setSlug(product.getSlug());
        jpaEntity.setDescription(product.getDescription());
        jpaEntity.setSku(product.getSku().getValue());
        jpaEntity.setBasePrice(product.getBasePrice().getAmount());
        jpaEntity.setCurrency(product.getBasePrice().getCurrency());

        if (product.getSalePrice() != null) {
            jpaEntity.setSalePrice(product.getSalePrice().getAmount());
        }

        jpaEntity.setIsActive(product.isActive());
        jpaEntity.setIsFeatured(product.isFeatured());
        jpaEntity.setCreatedAt(product.getCreatedAt());
        jpaEntity.setUpdatedAt(product.getUpdatedAt());

        return jpaEntity;
    }

    public Product toDomainEntity(ProductJpaEntity jpaEntity) {
        // Create domain entity using reflection to set private fields
        try {
            SKU sku = new SKU(jpaEntity.getSku());
            Price basePrice = new Price(jpaEntity.getBasePrice(), jpaEntity.getCurrency());

            Product product = new Product(
                    jpaEntity.getName(),
                    jpaEntity.getSlug(),
                    jpaEntity.getDescription(),
                    sku,
                    basePrice
            );

            // Set private fields using reflection
            setPrivateField(product, "id", jpaEntity.getId());
            setPrivateField(product, "isActive", jpaEntity.getIsActive());
            setPrivateField(product, "isFeatured", jpaEntity.getIsFeatured());
            setPrivateField(product, "createdAt", jpaEntity.getCreatedAt());
            setPrivateField(product, "updatedAt", jpaEntity.getUpdatedAt());

            if (jpaEntity.getSalePrice() != null) {
                Price salePrice = new Price(jpaEntity.getSalePrice(), jpaEntity.getCurrency());
                setPrivateField(product, "salePrice", salePrice);
            }

            return product;

        } catch (Exception e) {
            throw new RuntimeException("Error mapping JPA entity to domain entity", e);
        }
    }

    private void setPrivateField(Object object, String fieldName, Object value)
            throws NoSuchFieldException, IllegalAccessException {
        Field field = object.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(object, value);
    }
}

package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.entity.CartItem;
import com.ecommerce.adapter.persistence.entity.CartEntity;
import com.ecommerce.adapter.persistence.entity.CartItemEntity;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class CartMapper {

    public CartEntity toEntity(Cart cart) {
        if (cart == null) {
            return null;
        }

        CartEntity entity = new CartEntity();
        entity.setId(cart.getId());
        
        // Create a UserJpaEntity with the userId for the foreign key relationship
        if (cart.getUserId() != null) {
            com.ecommerce.adapter.persistence.jpa.entity.UserJpaEntity userEntity = 
                new com.ecommerce.adapter.persistence.jpa.entity.UserJpaEntity();
            userEntity.setId(cart.getUserId());
            entity.setUser(userEntity);
        }
        
        entity.setTotalAmount(cart.getTotalAmount());
        entity.setCreatedAt(cart.getCreatedAt());
        entity.setUpdatedAt(cart.getUpdatedAt());

        // Map cart items
        if (cart.getItems() != null) {
            List<CartItemEntity> itemEntities = cart.getItems().stream()
                .map(item -> toCartItemEntity(item, entity))
                .collect(Collectors.toList());
            entity.setItems(itemEntities);
        }

        return entity;
    }

    public Cart toDomain(CartEntity entity) {
        if (entity == null) {
            return null;
        }

        Cart cart = new Cart(entity.getUserId());

        // Use reflection to set private fields for persistence
        try {
            java.lang.reflect.Field idField = Cart.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(cart, entity.getId());

            java.lang.reflect.Field totalAmountField = Cart.class.getDeclaredField("totalAmount");
            totalAmountField.setAccessible(true);
            totalAmountField.set(cart, entity.getTotalAmount());

            java.lang.reflect.Field createdAtField = Cart.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(cart, entity.getCreatedAt());

            java.lang.reflect.Field updatedAtField = Cart.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(cart, entity.getUpdatedAt());

            // Map cart items
            if (entity.getItems() != null) {
                List<CartItem> items = entity.getItems().stream()
                    .map(this::toCartItemDomain)
                    .collect(Collectors.toList());
                
                java.lang.reflect.Field itemsField = Cart.class.getDeclaredField("items");
                itemsField.setAccessible(true);
                itemsField.set(cart, items);
            }

        } catch (Exception e) {
            throw new RuntimeException("Error mapping cart entity to domain", e);
        }

        return cart;
    }

    private CartItemEntity toCartItemEntity(CartItem item, CartEntity cartEntity) {
        CartItemEntity entity = new CartItemEntity();
        entity.setCart(cartEntity);
        entity.setProductId(item.getProductId());
        entity.setProductName(item.getProductName());
        entity.setProductSku(null); // CartItem doesn't have SKU, can be enhanced later
        entity.setQuantity(item.getQuantity());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setTotalPrice(item.getTotalPrice());
        return entity;
    }

    private CartItem toCartItemDomain(CartItemEntity entity) {
        CartItem item = new CartItem(
            entity.getProductId(),
            entity.getProductName(),
            entity.getUnitPrice(),
            entity.getQuantity()
        );

        // Use reflection to set private fields for persistence
        try {
            java.lang.reflect.Field idField = CartItem.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(item, entity.getId());

            // Note: CartItem doesn't have productSku field, skipping this mapping

        } catch (Exception e) {
            throw new RuntimeException("Error mapping cart item entity to domain", e);
        }

        return item;
    }
}
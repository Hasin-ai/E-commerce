package com.ecommerce.adapter.persistence.mapper;

import com.ecommerce.core.domain.order.entity.Order;
import com.ecommerce.core.domain.order.entity.OrderStatus;
import com.ecommerce.core.domain.order.entity.OrderItem;
import com.ecommerce.core.domain.order.entity.Address;
import com.ecommerce.core.domain.user.entity.User;
import com.ecommerce.core.domain.user.repository.UserRepository;
import com.ecommerce.adapter.persistence.entity.OrderEntity;
import com.ecommerce.adapter.persistence.entity.OrderItemEntity;
import com.ecommerce.adapter.persistence.entity.OrderAddressEntity;

import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class OrderMapper {

    private final UserRepository userRepository;

    public OrderMapper(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public OrderEntity toEntity(Order order) {
        if (order == null) {
            return null;
        }

        OrderEntity entity = new OrderEntity();
        entity.setId(order.getId());
        entity.setOrderNumber(order.getOrderNumber());
        entity.setUserId(order.getUserId());
        entity.setStatus(mapOrderStatus(order.getStatus()));
        entity.setSubtotalAmount(order.getSubtotalAmount());
        entity.setTaxAmount(order.getTaxAmount());
        entity.setShippingAmount(order.getShippingAmount());
        entity.setDiscountAmount(order.getDiscountAmount());
        entity.setTotalAmount(order.getTotalAmount());
        entity.setCurrency(order.getCurrency());

        // Set default values for fields not in domain entity yet
        entity.setPaymentStatus("PENDING"); // Default payment status
        entity.setFulfillmentStatus("UNFULFILLED"); // Default fulfillment status
        entity.setNotes(null); // Not in domain entity yet
        entity.setInternalNotes(null); // Not in domain entity yet
        entity.setCancelledAt(null); // Not in domain entity yet
        entity.setCancelledReason(null); // Not in domain entity yet
        entity.setShippedAt(null); // Not in domain entity yet
        entity.setDeliveredAt(null); // Not available in domain entity
        entity.setCreatedAt(order.getCreatedAt());
        entity.setUpdatedAt(order.getUpdatedAt());

        // Map order items
        List<OrderItemEntity> itemEntities = order.getItems().stream()
            .map(item -> toOrderItemEntity(item, entity))
            .collect(Collectors.toList());
        entity.setItems(itemEntities);

        // Get user information for addresses
        User user = userRepository.findById(order.getUserId())
            .orElse(null);

        // Map addresses
        List<OrderAddressEntity> addressEntities = List.of(
            toAddressEntity(order.getShippingAddress(), entity, OrderAddressEntity.AddressType.SHIPPING, user),
            toAddressEntity(order.getBillingAddress(), entity, OrderAddressEntity.AddressType.BILLING, user)
        );
        entity.setAddresses(addressEntities);

        return entity;
    }

    public Order toDomain(OrderEntity entity) {
        if (entity == null) {
            return null;
        }

        // Extract addresses
        Address shippingAddress = null;
        Address billingAddress = null;
        
        if (entity.getAddresses() != null) {
            for (OrderAddressEntity addressEntity : entity.getAddresses()) {
                Address address = new Address(
                    addressEntity.getAddressLine1(),
                    addressEntity.getCity(),
                    addressEntity.getState(),
                    addressEntity.getPostalCode(),
                    addressEntity.getCountry()
                );
                
                if (addressEntity.getAddressType() == OrderAddressEntity.AddressType.SHIPPING) {
                    shippingAddress = address;
                } else if (addressEntity.getAddressType() == OrderAddressEntity.AddressType.BILLING) {
                    billingAddress = address;
                }
            }
        }

        // Create a mock cart for order creation (this is a limitation of the current design)
        // In a real scenario, you might want to store cart data or refactor the Order constructor
        com.ecommerce.core.domain.cart.entity.Cart mockCart = new com.ecommerce.core.domain.cart.entity.Cart(entity.getUserId());
        
        // Add items to mock cart
        if (entity.getItems() != null) {
            entity.getItems().forEach(itemEntity -> {
                mockCart.addItem(
                    itemEntity.getProductId(),
                    itemEntity.getProductName(),
                    itemEntity.getUnitPrice(),
                    itemEntity.getQuantity()
                );
            });
        }

        Order order = new Order(
            entity.getOrderNumber(),
            entity.getUserId(),
            mockCart,
            shippingAddress != null ? shippingAddress : new Address("", "", "", "", ""),
            billingAddress != null ? billingAddress : new Address("", "", "", "", "")
        );

        // Use reflection to set private fields for persistence
        try {
            java.lang.reflect.Field idField = Order.class.getDeclaredField("id");
            idField.setAccessible(true);
            idField.set(order, entity.getId());

            java.lang.reflect.Field statusField = Order.class.getDeclaredField("status");
            statusField.setAccessible(true);
            statusField.set(order, mapOrderStatus(entity.getStatus()));

            java.lang.reflect.Field createdAtField = Order.class.getDeclaredField("createdAt");
            createdAtField.setAccessible(true);
            createdAtField.set(order, entity.getCreatedAt());

            java.lang.reflect.Field updatedAtField = Order.class.getDeclaredField("updatedAt");
            updatedAtField.setAccessible(true);
            updatedAtField.set(order, entity.getUpdatedAt());

            java.lang.reflect.Field trackingNumberField = Order.class.getDeclaredField("trackingNumber");
            trackingNumberField.setAccessible(true);
            trackingNumberField.set(order, entity.getTrackingNumber());

            java.lang.reflect.Field estimatedDeliveryField = Order.class.getDeclaredField("estimatedDelivery");
            estimatedDeliveryField.setAccessible(true);
            estimatedDeliveryField.set(order, entity.getEstimatedDelivery());

        } catch (Exception e) {
            throw new RuntimeException("Error mapping order entity to domain", e);
        }

        return order;
    }

    private OrderItemEntity toOrderItemEntity(OrderItem item, OrderEntity orderEntity) {
        OrderItemEntity entity = new OrderItemEntity();
        entity.setOrder(orderEntity);
        entity.setProductId(item.getProductId());
        entity.setProductName(item.getProductName());
        entity.setProductSku(item.getProductSku() != null ? item.getProductSku() : "SKU-" + item.getProductId());
        entity.setQuantity(item.getQuantity());
        entity.setUnitPrice(item.getUnitPrice());
        entity.setTotalPrice(item.getTotalPrice());
        return entity;
    }

    private OrderAddressEntity toAddressEntity(Address address, OrderEntity orderEntity, OrderAddressEntity.AddressType type, User user) {
        OrderAddressEntity entity = new OrderAddressEntity();
        entity.setOrder(orderEntity);
        entity.setAddressType(type);
        entity.setAddressLine1(address.getStreet());
        entity.setCity(address.getCity());
        entity.setState(address.getState());
        entity.setPostalCode(address.getZipCode());
        entity.setCountry(address.getCountry());
        
        // Set user information for the address
        if (user != null) {
            entity.setFirstName(user.getFirstName());
            entity.setLastName(user.getLastName());
            if (user.getPhone() != null) {
                entity.setPhone(user.getPhone().getValue());
            }
        } else {
            // Fallback values
            entity.setFirstName("Customer");
            entity.setLastName("User");
        }
        
        entity.setCreatedAt(java.time.LocalDateTime.now());
        
        return entity;
    }

    private OrderEntity.OrderStatusEntity mapOrderStatus(OrderStatus status) {
        if (status == null) {
            return null;
        }
        return OrderEntity.OrderStatusEntity.valueOf(status.name());
    }

    private OrderStatus mapOrderStatus(OrderEntity.OrderStatusEntity status) {
        if (status == null) {
            return null;
        }
        return OrderStatus.valueOf(status.name());
    }
}

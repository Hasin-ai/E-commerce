package com.ecommerce.core.usecase.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class CreateOrderRequest {
    private Long userId;
    private List<OrderItemRequest> items;

    @Getter
    @Setter
    @Builder
    public static class OrderItemRequest {
        private Long productId;
        private Integer quantity;
    }
}

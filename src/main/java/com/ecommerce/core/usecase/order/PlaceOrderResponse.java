package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.valueobject.OrderStatus;
import com.ecommerce.core.domain.product.valueobject.Price;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class PlaceOrderResponse {
    private Long orderId;
    private OrderStatus status;
    private Price total;
    private LocalDateTime updatedAt;
    private String message;
}

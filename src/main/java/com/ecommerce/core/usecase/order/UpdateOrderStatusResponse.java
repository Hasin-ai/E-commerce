package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.valueobject.OrderStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class UpdateOrderStatusResponse {
    private Long orderId;
    private OrderStatus status;
    private LocalDateTime updatedAt;
    private String message;
}

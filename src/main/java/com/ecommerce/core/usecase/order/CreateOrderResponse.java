package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.entity.OrderItem;
import com.ecommerce.core.domain.order.valueobject.OrderStatus;
import com.ecommerce.core.domain.product.valueobject.Price;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class CreateOrderResponse {
    private Long id;
    private Long userId;
    private List<OrderItem> items;
    private Price total;
    private OrderStatus status;
    private LocalDateTime createdAt;
}

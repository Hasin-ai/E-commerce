package com.ecommerce.core.usecase.order;

import com.ecommerce.core.domain.order.valueobject.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateOrderStatusRequest {
    private Long orderId;
    private OrderStatus newStatus;
}

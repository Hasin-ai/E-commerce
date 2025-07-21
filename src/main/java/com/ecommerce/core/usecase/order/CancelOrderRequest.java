package com.ecommerce.core.usecase.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CancelOrderRequest {
    private Long orderId;
    private Long userId; // Optional - for access control
    private String reason; // Optional cancellation reason
}

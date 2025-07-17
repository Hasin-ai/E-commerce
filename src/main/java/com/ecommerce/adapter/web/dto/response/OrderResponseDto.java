package com.ecommerce.adapter.web.dto.response;

import com.ecommerce.core.domain.order.valueobject.OrderStatus;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class OrderResponseDto {

    private Long id;

    @JsonProperty("user_id")
    private Long userId;

    private List<OrderItemResponseDto> items;

    private BigDecimal total;

    private OrderStatus status;

    @JsonProperty("created_at")
    private LocalDateTime createdAt;

    @JsonProperty("updated_at")
    private LocalDateTime updatedAt;
}

package com.ecommerce.core.usecase.order;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class GetUserOrdersRequest {
    private Long userId;
}

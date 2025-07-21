package com.ecommerce.core.usecase.order;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
public class GetOrderListResponse {
    private List<GetOrderResponse> orders;
    private int totalCount;
}

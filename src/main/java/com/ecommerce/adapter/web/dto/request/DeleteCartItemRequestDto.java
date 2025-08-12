package com.ecommerce.adapter.web.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DeleteCartItemRequestDto {
    // This DTO is intentionally simple as the cart item ID comes from the path parameter
    // No additional request body fields are needed for deletion
    private String reason; // Optional field for tracking deletion reasons
}

package com.ecommerce.core.usecase.vendor;

import com.ecommerce.adapter.web.dto.response.VendorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVendorResponse {
    private VendorResponseDto vendor;
    private boolean success;
    private String message;
}
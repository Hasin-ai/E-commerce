package com.ecommerce.core.usecase.vendor;

import com.ecommerce.adapter.web.dto.response.VendorResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GetVendorsResponse {
    private List<VendorResponseDto> vendors;
}
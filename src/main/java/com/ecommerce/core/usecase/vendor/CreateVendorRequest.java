package com.ecommerce.core.usecase.vendor;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateVendorRequest {
    private String name;
    private String email;
    private String phone;
    private String website;
    private String description;
    private String businessLicense;
    private String taxId;
    private BigDecimal commissionRate;
}
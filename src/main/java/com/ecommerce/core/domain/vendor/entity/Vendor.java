package com.ecommerce.core.domain.vendor.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Vendor {
    private Long id;
    private String name;
    private String slug;
    private String description;
    private String email;
    private String phone;
    private String website;
    private VendorStatus status;
    private BigDecimal commissionRate;
    private String businessLicense;
    private String taxId;
    private Address address;
    private BankDetails bankDetails;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime approvedAt;
}
package com.ecommerce.adapter.web.dto.response;

import com.ecommerce.core.domain.vendor.entity.VendorStatus;
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
public class VendorResponseDto {
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
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private LocalDateTime approvedAt;
}
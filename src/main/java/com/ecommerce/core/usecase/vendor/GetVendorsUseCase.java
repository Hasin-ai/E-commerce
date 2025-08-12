package com.ecommerce.core.usecase.vendor;

import com.ecommerce.adapter.web.dto.response.VendorResponseDto;
import com.ecommerce.core.domain.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GetVendorsUseCase {
    
    private final VendorRepository vendorRepository;
    
    public GetVendorsResponse execute() {
        var vendors = vendorRepository.findAll();
        
        List<VendorResponseDto> vendorDtos = vendors.stream()
                .map(vendor -> VendorResponseDto.builder()
                        .id(vendor.getId())
                        .name(vendor.getName())
                        .slug(vendor.getSlug())
                        .email(vendor.getEmail())
                        .phone(vendor.getPhone())
                        .website(vendor.getWebsite())
                        .description(vendor.getDescription())
                        .status(vendor.getStatus())
                        .commissionRate(vendor.getCommissionRate())
                        .businessLicense(vendor.getBusinessLicense())
                        .taxId(vendor.getTaxId())
                        .createdAt(vendor.getCreatedAt())
                        .updatedAt(vendor.getUpdatedAt())
                        .approvedAt(vendor.getApprovedAt())
                        .build())
                .toList();
        
        return GetVendorsResponse.builder()
                .vendors(vendorDtos)
                .build();
    }
}
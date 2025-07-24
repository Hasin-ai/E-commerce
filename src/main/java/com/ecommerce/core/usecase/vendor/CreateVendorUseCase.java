package com.ecommerce.core.usecase.vendor;

import com.ecommerce.adapter.web.dto.response.VendorResponseDto;
import com.ecommerce.core.domain.vendor.entity.Vendor;
import com.ecommerce.core.domain.vendor.entity.VendorStatus;
import com.ecommerce.core.domain.vendor.repository.VendorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class CreateVendorUseCase {
    
    private final VendorRepository vendorRepository;
    
    public CreateVendorResponse execute(CreateVendorRequest request) {
        try {
            // Check if vendor with email already exists
            if (vendorRepository.findByEmail(request.getEmail()).isPresent()) {
                return CreateVendorResponse.builder()
                        .success(false)
                        .message("Vendor with this email already exists")
                        .build();
            }
            
            // Create slug from name
            String slug = request.getName().toLowerCase()
                    .replaceAll("[^a-z0-9\\s]", "")
                    .replaceAll("\\s+", "-");
            
            Vendor vendor = Vendor.builder()
                    .name(request.getName())
                    .slug(slug)
                    .email(request.getEmail())
                    .phone(request.getPhone())
                    .website(request.getWebsite())
                    .description(request.getDescription())
                    .businessLicense(request.getBusinessLicense())
                    .taxId(request.getTaxId())
                    .commissionRate(request.getCommissionRate())
                    .status(VendorStatus.PENDING)
                    .createdAt(LocalDateTime.now())
                    .updatedAt(LocalDateTime.now())
                    .build();
            
            Vendor savedVendor = vendorRepository.save(vendor);
            
            VendorResponseDto responseDto = VendorResponseDto.builder()
                    .id(savedVendor.getId())
                    .name(savedVendor.getName())
                    .slug(savedVendor.getSlug())
                    .email(savedVendor.getEmail())
                    .phone(savedVendor.getPhone())
                    .website(savedVendor.getWebsite())
                    .description(savedVendor.getDescription())
                    .status(savedVendor.getStatus())
                    .commissionRate(savedVendor.getCommissionRate())
                    .businessLicense(savedVendor.getBusinessLicense())
                    .taxId(savedVendor.getTaxId())
                    .createdAt(savedVendor.getCreatedAt())
                    .updatedAt(savedVendor.getUpdatedAt())
                    .approvedAt(savedVendor.getApprovedAt())
                    .build();
            
            return CreateVendorResponse.builder()
                    .vendor(responseDto)
                    .success(true)
                    .message("Vendor created successfully")
                    .build();
                    
        } catch (Exception e) {
            return CreateVendorResponse.builder()
                    .success(false)
                    .message("Failed to create vendor: " + e.getMessage())
                    .build();
        }
    }
}
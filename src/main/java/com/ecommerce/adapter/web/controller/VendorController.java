package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.CreateVendorRequestDto;
import com.ecommerce.adapter.web.dto.response.VendorResponseDto;
import com.ecommerce.core.usecase.vendor.CreateVendorRequest;
import com.ecommerce.core.usecase.vendor.CreateVendorUseCase;
import com.ecommerce.core.usecase.vendor.GetVendorsUseCase;
import com.ecommerce.shared.dto.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/api/vendors")
@RequiredArgsConstructor
public class VendorController {
    
    private final CreateVendorUseCase createVendorUseCase;
    private final GetVendorsUseCase getVendorsUseCase;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<VendorResponseDto>>> getAllVendors() {
        var response = getVendorsUseCase.execute();
        return ResponseEntity.ok(ApiResponse.success(response.getVendors(), "Vendors retrieved successfully"));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<VendorResponseDto>> getVendorById(@PathVariable Long id) {
        // TODO: Implement GetVendorByIdUseCase
        return ResponseEntity.ok(ApiResponse.success(null, "Vendor retrieved successfully"));
    }
    
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<VendorResponseDto>> createVendor(@Valid @RequestBody CreateVendorRequestDto request) {
        CreateVendorRequest useCaseRequest = CreateVendorRequest.builder()
                .name(request.getName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .website(request.getWebsite())
                .description(request.getDescription())
                .businessLicense(request.getBusinessLicense())
                .taxId(request.getTaxId())
                .commissionRate(request.getCommissionRate())
                .build();
        
        var response = createVendorUseCase.execute(useCaseRequest);
        return ResponseEntity.ok(ApiResponse.success(response.getVendor(), "Vendor created successfully"));
    }
    
    @PutMapping("/{id}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> approveVendor(@PathVariable Long id) {
        // TODO: Implement ApproveVendorUseCase
        return ResponseEntity.ok(ApiResponse.success(null, "Vendor approved successfully"));
    }
    
    @PutMapping("/{id}/suspend")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> suspendVendor(@PathVariable Long id) {
        // TODO: Implement SuspendVendorUseCase
        return ResponseEntity.ok(ApiResponse.success(null, "Vendor suspended successfully"));
    }
}
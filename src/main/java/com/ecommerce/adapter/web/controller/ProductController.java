package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.CreateProductRequestDto;
import com.ecommerce.adapter.web.dto.request.UpdateProductRequestDto;
import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import com.ecommerce.shared.dto.ApiResponse;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

@RestController
@RequestMapping("/api/products")
@Validated
public class ProductController {

    // TODO: Inject use cases when implemented
    // private final CreateProductUseCase createProductUseCase;
    // private final GetProductUseCase getProductUseCase;
    // private final UpdateProductUseCase updateProductUseCase;
    // private final DeleteProductUseCase deleteProductUseCase;
    // private final SearchProductsUseCase searchProductsUseCase;

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getAllProducts(
            Pageable pageable,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean featured) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Products retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(
            @PathVariable @NotNull @Positive Long id) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Product retrieved successfully"));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductBySlug(
            @PathVariable String slug) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Product retrieved successfully"));
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody CreateProductRequestDto requestDto) {
        
        // TODO: Implement with use case
        return ResponseEntity
            .status(HttpStatus.CREATED)
            .body(ApiResponse.success(null, "Product created successfully"));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<ProductResponseDto>> updateProduct(
            @PathVariable @NotNull @Positive Long id,
            @Valid @RequestBody UpdateProductRequestDto requestDto) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable @NotNull @Positive Long id) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }

    @GetMapping("/featured")
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getFeaturedProducts(
            Pageable pageable) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Featured products retrieved successfully"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getProductsByCategory(
            @PathVariable @NotNull @Positive Long categoryId,
            Pageable pageable) {
        
        // TODO: Implement with use case
        return ResponseEntity.ok(ApiResponse.success(null, "Products by category retrieved successfully"));
    }
}
package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.CreateProductRequestDto;
import com.ecommerce.adapter.web.dto.request.UpdateProductRequestDto;
import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import com.ecommerce.core.usecase.product.GetProductUseCase;
import com.ecommerce.core.usecase.product.GetProductRequest;
import com.ecommerce.core.usecase.product.GetProductResponse;
import com.ecommerce.core.usecase.product.GetProductsUseCase;
import com.ecommerce.core.usecase.product.GetProductsRequest;
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

    private final GetProductUseCase getProductUseCase;
    private final GetProductsUseCase getProductsUseCase;

    public ProductController(GetProductUseCase getProductUseCase, GetProductsUseCase getProductsUseCase) {
        this.getProductUseCase = getProductUseCase;
        this.getProductsUseCase = getProductsUseCase;
    }

    @GetMapping
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getAllProducts(
            Pageable pageable,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Boolean featured) {
        
        Long categoryId = category != null ? Long.parseLong(category) : null;
        GetProductsRequest request = new GetProductsRequest(categoryId, search, featured);
        
        Page<GetProductResponse> products = getProductsUseCase.execute(request, pageable);
        Page<ProductResponseDto> productDtos = products.map(this::mapToProductResponseDto);
        
        return ResponseEntity.ok(ApiResponse.success(productDtos, "Products retrieved successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductById(
            @PathVariable @NotNull @Positive Long id) {
        
        GetProductRequest request = new GetProductRequest(id);
        GetProductResponse product = getProductUseCase.execute(request);
        ProductResponseDto productDto = mapToProductResponseDto(product);
        
        return ResponseEntity.ok(ApiResponse.success(productDto, "Product retrieved successfully"));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductBySlug(
            @PathVariable String slug) {
        
        GetProductResponse product = getProductUseCase.executeBySlug(slug);
        ProductResponseDto productDto = mapToProductResponseDto(product);
        
        return ResponseEntity.ok(ApiResponse.success(productDto, "Product retrieved successfully"));
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
        
        Page<GetProductResponse> products = getProductsUseCase.getFeaturedProducts(pageable);
        Page<ProductResponseDto> productDtos = products.map(this::mapToProductResponseDto);
        
        return ResponseEntity.ok(ApiResponse.success(productDtos, "Featured products retrieved successfully"));
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<ApiResponse<Page<ProductResponseDto>>> getProductsByCategory(
            @PathVariable @NotNull @Positive Long categoryId,
            Pageable pageable) {
        
        Page<GetProductResponse> products = getProductsUseCase.getProductsByCategory(categoryId, pageable);
        Page<ProductResponseDto> productDtos = products.map(this::mapToProductResponseDto);
        
        return ResponseEntity.ok(ApiResponse.success(productDtos, "Products by category retrieved successfully"));
    }

    private ProductResponseDto mapToProductResponseDto(GetProductResponse product) {
        return ProductResponseDto.builder()
                .id(product.getId())
                .name(product.getName())
                .slug(product.getSlug())
                .description(product.getDescription())
                .sku(product.getSku())
                .price(product.getPrice())
                .discountPrice(product.getDiscountPrice())
                .currency(product.getCurrency())
                .categoryId(product.getCategoryId())
                .category(product.getCategory())
                .imageUrl(product.getImageUrl())
                .stockQuantity(product.getStockQuantity())
                .inStock(product.getStockQuantity() > 0)
                .isActive(product.isActive())
                .isFeatured(product.isFeatured())
                .createdAt(product.getCreatedAt())
                .updatedAt(product.getUpdatedAt())
                .build();
    }
}
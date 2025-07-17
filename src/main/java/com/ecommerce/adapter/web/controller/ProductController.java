package com.ecommerce.adapter.web.controller;

import com.ecommerce.adapter.web.dto.request.CreateProductRequestDto;
import com.ecommerce.adapter.web.dto.request.UpdateProductRequestDto;
import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import com.ecommerce.adapter.web.mapper.ProductMapper;
import com.ecommerce.core.usecase.product.*;
import com.ecommerce.shared.dto.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/products")
@Validated
@RequiredArgsConstructor
public class ProductController {

    private final CreateProductUseCase createProductUseCase;
    private final GetProductUseCase getProductUseCase;
    private final GetAllProductsUseCase getAllProductsUseCase;
    private final UpdateProductUseCase updateProductUseCase;
    private final DeleteProductUseCase deleteProductUseCase;
    private final ProductMapper productMapper;

    @PostMapping
    public ResponseEntity<ApiResponse<ProductResponseDto>> createProduct(
            @Valid @RequestBody CreateProductRequestDto requestDto) {

        CreateProductUseCase.CreateProductRequest request =
                productMapper.toCreateProductRequest(requestDto);

        CreateProductUseCase.CreateProductResponse response =
                createProductUseCase.execute(request);

        ProductResponseDto responseDto = productMapper.toProductResponse(response);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(responseDto, "Product created successfully"));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProduct(
            @PathVariable @NotNull @Positive Long id) {

        GetProductUseCase.GetProductResponse response =
                getProductUseCase.execute(id);

        ProductResponseDto responseDto = productMapper.toProductResponse(response);

        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @GetMapping("/slug/{slug}")
    public ResponseEntity<ApiResponse<ProductResponseDto>> getProductBySlug(
            @PathVariable @NotNull String slug) {

        GetProductUseCase.GetProductResponse response =
                getProductUseCase.executeBySlug(slug);

        ProductResponseDto responseDto = productMapper.toProductResponse(response);

        return ResponseEntity.ok(ApiResponse.success(responseDto));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ProductResponseDto>>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        List<GetProductUseCase.GetProductResponse> responses =
                getAllProductsUseCase.execute(page, size);

        List<ProductResponseDto> responseDtos = responses.stream()
                .map(productMapper::toProductResponse)
                .collect(Collectors.toList());

        return ResponseEntity.ok(ApiResponse.success(responseDtos));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> updateProduct(
            @PathVariable @NotNull @Positive Long id,
            @Valid @RequestBody UpdateProductRequestDto requestDto) {

        UpdateProductUseCase.UpdateProductRequest request =
                productMapper.toUpdateProductRequest(requestDto);

        updateProductUseCase.execute(id, request);

        return ResponseEntity.ok(ApiResponse.success(null, "Product updated successfully"));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteProduct(
            @PathVariable @NotNull @Positive Long id) {

        deleteProductUseCase.execute(id);

        return ResponseEntity.ok(ApiResponse.success(null, "Product deleted successfully"));
    }
}

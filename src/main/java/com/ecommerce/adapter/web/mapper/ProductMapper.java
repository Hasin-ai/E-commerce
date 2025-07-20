package com.ecommerce.adapter.web.mapper;

import com.ecommerce.adapter.web.dto.request.CreateProductRequestDto;
import com.ecommerce.adapter.web.dto.request.UpdateProductRequestDto;
import com.ecommerce.adapter.web.dto.response.ProductResponseDto;
import com.ecommerce.core.usecase.product.CreateProductUseCase;
import com.ecommerce.core.usecase.product.GetProductUseCase;
import com.ecommerce.core.usecase.product.UpdateProductUseCase;
import com.ecommerce.core.domain.product.entity.Product;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

    public CreateProductUseCase.CreateProductRequest toCreateProductRequest(CreateProductRequestDto dto) {
        CreateProductUseCase.CreateProductRequest request = new CreateProductUseCase.CreateProductRequest();
        request.setName(dto.getName());
        request.setSlug(dto.getSlug());
        request.setDescription(dto.getDescription());
        request.setSku(dto.getSku());
        request.setBasePrice(dto.getBasePrice());
        request.setSalePrice(dto.getSalePrice());
        request.setCurrency(dto.getCurrency());
        return request;
    }

    public UpdateProductUseCase.UpdateProductRequest toUpdateProductRequest(UpdateProductRequestDto dto) {
        UpdateProductUseCase.UpdateProductRequest request = new UpdateProductUseCase.UpdateProductRequest();
        request.setName(dto.getName());
        request.setSlug(dto.getSlug());
        request.setDescription(dto.getDescription());
        request.setSku(dto.getSku());
        request.setBasePrice(dto.getBasePrice());
        request.setSalePrice(dto.getSalePrice());
        request.setCurrency(dto.getCurrency());
        request.setIsActive(dto.getIsActive());
        request.setIsFeatured(dto.getIsFeatured());
        return request;
    }

    public ProductResponseDto toProductResponse(CreateProductUseCase.CreateProductResponse response) {
        return new ProductResponseDto(
                response.getId(),
                response.getName(),
                response.getSlug(),
                null, // description not included in create response
                response.getSku(),
                response.getBasePrice(),
                null, // sale price not included in create response
                response.getCurrency(),
                response.isActive(),
                false, // featured not included in create response
                response.getCreatedAt(),
                response.getCreatedAt() // updated at same as created at initially
        );
    }

    public ProductResponseDto toProductResponse(GetProductUseCase.GetProductResponse response) {
        return new ProductResponseDto(
                response.getId(),
                response.getName(),
                response.getSlug(),
                response.getDescription(),
                response.getSku(),
                response.getBasePrice(),
                response.getSalePrice(),
                response.getCurrency(),
                response.isActive(),
                response.isFeatured(),
                response.getCreatedAt(),
                response.getUpdatedAt()
        );
    }

    public ProductResponseDto toProductResponse(Product product) {
        return new ProductResponseDto(
                product.getId(),
                product.getName(),
                product.getSlug(),
                product.getDescription(),
                product.getSku().getValue(),
                product.getBasePrice().getAmount(),
                product.getSalePrice() != null ? product.getSalePrice().getAmount() : null,
                product.getBasePrice().getCurrency(),
                product.isActive(),
                product.isFeatured(),
                product.getCreatedAt(),
                product.getUpdatedAt()
        );
    }
}

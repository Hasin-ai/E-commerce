package com.ecommerce.core.usecase.product;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.adapter.persistence.implementation.ProductRepositoryImpl;
import com.ecommerce.shared.exception.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetProductUseCase {

    private final ProductRepositoryImpl productRepository;

    public GetProductUseCase(ProductRepositoryImpl productRepository) {
        this.productRepository = productRepository;
    }

    public GetProductResponse execute(GetProductRequest request) {
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new NotFoundException("Product not found"));

        return mapToResponse(product);
    }

    public GetProductResponse executeBySlug(String slug) {
        Product product = productRepository.findBySlug(slug)
            .orElseThrow(() -> new NotFoundException("Product not found"));

        return mapToResponse(product);
    }

    private GetProductResponse mapToResponse(Product product) {
        return new GetProductResponse(
            product.getId(),
            product.getName(),
            product.getDescription(),
            product.getSku(),
            product.getPrice().getAmount(),
            null, // discountPrice - can be enhanced later
            product.getCategoryId() != null ? Long.parseLong(product.getCategoryId()) : null,
            product.getImageUrls() != null && !product.getImageUrls().isEmpty() 
                ? product.getImageUrls().get(0) : null,
            product.getStockQuantity(),
            product.isActive(),
            false, // featured - can be enhanced later
            product.getCreatedAt(),
            product.getUpdatedAt()
        );
    }
}
package com.ecommerce.core.usecase.product;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.adapter.persistence.implementation.ProductRepositoryImpl;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class GetProductsUseCase {

    private final ProductRepositoryImpl productRepository;

    public GetProductsUseCase(ProductRepositoryImpl productRepository) {
        this.productRepository = productRepository;
    }

    public Page<GetProductResponse> execute(GetProductsRequest request, Pageable pageable) {
        Page<Product> products;

        if (request.getCategoryId() != null) {
            products = productRepository.findByCategoryId(request.getCategoryId(), pageable);
        } else if (request.getFeatured() != null && request.getFeatured()) {
            products = productRepository.findByFeatured(true, pageable);
        } else if (request.getSearch() != null && !request.getSearch().trim().isEmpty()) {
            products = productRepository.findByNameContainingIgnoreCase(request.getSearch(), pageable);
        } else {
            products = productRepository.findByActive(true, pageable);
        }

        return products.map(this::mapToResponse);
    }

    public Page<GetProductResponse> getFeaturedProducts(Pageable pageable) {
        Page<Product> products = productRepository.findByFeatured(true, pageable);
        return products.map(this::mapToResponse);
    }

    public Page<GetProductResponse> getProductsByCategory(Long categoryId, Pageable pageable) {
        Page<Product> products = productRepository.findByCategoryId(categoryId, pageable);
        return products.map(this::mapToResponse);
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
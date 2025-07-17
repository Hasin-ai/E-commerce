package com.ecommerce.core.usecase.product;

import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class GetAllProductsUseCase {

    private final ProductRepository productRepository;

    public List<GetProductUseCase.GetProductResponse> execute(int page, int size) {
        List<Product> products = productRepository.findAll(page, size);
        return products.stream()
                .map(this::toGetProductResponse)
                .collect(Collectors.toList());
    }

    private GetProductUseCase.GetProductResponse toGetProductResponse(Product product) {
        return new GetProductUseCase.GetProductResponse(
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

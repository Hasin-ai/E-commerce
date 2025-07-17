package com.ecommerce.core.usecase.product;

import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class DeleteProductUseCase {

    private final ProductRepository productRepository;

    public void execute(Long productId) {
        if (!productRepository.existsById(productId)) {
            throw new ResourceNotFoundException("Product not found with id: " + productId);
        }
        productRepository.deleteById(productId);
    }
}

package com.ecommerce.core.usecase.cart;

import com.ecommerce.core.domain.cart.entity.Cart;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.core.domain.product.entity.Product;
import com.ecommerce.adapter.persistence.implementation.ProductRepositoryImpl;
import com.ecommerce.shared.exception.BusinessException;
import com.ecommerce.shared.exception.NotFoundException;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AddCartItemUseCase {

    private final CartRepository cartRepository;
    private final ProductRepositoryImpl productRepository;

    public AddCartItemUseCase(CartRepository cartRepository, ProductRepositoryImpl productRepository) {
        this.cartRepository = cartRepository;
        this.productRepository = productRepository;
    }

    public AddCartItemResponse execute(AddCartItemRequest request) {
        // Validate product exists and is available
        Product product = productRepository.findById(request.getProductId())
            .orElseThrow(() -> new NotFoundException("Product not found"));

        if (!product.isActive()) {
            throw new BusinessException("Product is not available");
        }

        if (product.getStockQuantity() < request.getQuantity()) {
            throw new BusinessException("Insufficient stock available");
        }

        // Get or create cart
        Cart cart = cartRepository.findByUserId(request.getUserId())
            .orElse(new Cart(request.getUserId()));

        // Add item to cart
        cart.addItem(
            product.getId(),
            product.getName(),
            product.getPrice().getAmount(),
            request.getQuantity()
        );

        // Save cart
        cart = cartRepository.save(cart);

        return new AddCartItemResponse(
            cart.getId(),
            cart.getUserId(),
            cart.getItems(),
            cart.getTotalAmount(),
            cart.getTotalItems(),
            "Item added to cart successfully"
        );
    }
}
package com.ecommerce.infrastructure.config;

import com.ecommerce.core.domain.payment.repository.PaymentRepository;
import com.ecommerce.core.domain.order.repository.OrderRepository;
import com.ecommerce.core.domain.cart.repository.CartRepository;
import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.usecase.payment.ProcessPaymentUseCase;
import com.ecommerce.core.usecase.payment.GetPaymentUseCase;
import com.ecommerce.core.usecase.order.CreateOrderUseCase;
import com.ecommerce.core.usecase.order.GetOrderUseCase;
import com.ecommerce.core.usecase.checkout.CheckoutWorkflowUseCase;
import com.ecommerce.core.usecase.product.GetProductUseCase;
import com.ecommerce.core.usecase.product.GetProductsUseCase;
import com.ecommerce.core.usecase.cart.GetCartUseCase;
import com.ecommerce.core.usecase.cart.AddCartItemUseCase;
import com.ecommerce.infrastructure.external.payment.PaymentGatewayService;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PaymentConfig {

    @Bean
    public ProcessPaymentUseCase processPaymentUseCase(
            PaymentRepository paymentRepository,
            OrderRepository orderRepository,
            PaymentGatewayService paymentGatewayService) {
        return new ProcessPaymentUseCase(paymentRepository, orderRepository, paymentGatewayService);
    }

    @Bean
    public GetPaymentUseCase getPaymentUseCase(PaymentRepository paymentRepository) {
        return new GetPaymentUseCase(paymentRepository);
    }

    @Bean
    public CreateOrderUseCase createOrderUseCase(
            OrderRepository orderRepository,
            CartRepository cartRepository) {
        return new CreateOrderUseCase(orderRepository, cartRepository);
    }

    @Bean
    public GetOrderUseCase getOrderUseCase(OrderRepository orderRepository) {
        return new GetOrderUseCase(orderRepository);
    }

    @Bean
    public CheckoutWorkflowUseCase checkoutWorkflowUseCase(
            CartRepository cartRepository,
            OrderRepository orderRepository,
            PaymentRepository paymentRepository,
            PaymentGatewayService paymentGatewayService) {
        return new CheckoutWorkflowUseCase(cartRepository, orderRepository, paymentRepository, paymentGatewayService);
    }

    @Bean
    public GetProductUseCase getProductUseCase(com.ecommerce.adapter.persistence.implementation.ProductRepositoryImpl productRepository) {
        return new GetProductUseCase(productRepository);
    }

    @Bean
    public GetProductsUseCase getProductsUseCase(com.ecommerce.adapter.persistence.implementation.ProductRepositoryImpl productRepository) {
        return new GetProductsUseCase(productRepository);
    }

    @Bean
    public GetCartUseCase getCartUseCase(CartRepository cartRepository) {
        return new GetCartUseCase(cartRepository);
    }

    @Bean
    public AddCartItemUseCase addCartItemUseCase(
            CartRepository cartRepository,
            com.ecommerce.adapter.persistence.implementation.ProductRepositoryImpl productRepository) {
        return new AddCartItemUseCase(cartRepository, productRepository);
    }
}
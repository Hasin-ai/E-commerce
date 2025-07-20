package com.ecommerce.infrastructure.config;

import com.ecommerce.core.domain.product.repository.ProductRepository;
import com.ecommerce.core.domain.recommendation.repository.RecommendationRepository;
import com.ecommerce.core.domain.search.repository.SearchRepository;
import com.ecommerce.core.usecase.product.*;
import com.ecommerce.core.usecase.recommendation.GetRecommendationsUseCase;
import com.ecommerce.core.usecase.search.SearchProductUseCase;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfig {

    @Bean
    public CreateProductUseCase createProductUseCase(ProductRepository productRepository) {
        return new CreateProductUseCase(productRepository);
    }

    @Bean
    public GetProductUseCase getProductUseCase(ProductRepository productRepository) {
        return new GetProductUseCase(productRepository);
    }

    @Bean
    public GetAllProductsUseCase getAllProductsUseCase(ProductRepository productRepository) {
        return new GetAllProductsUseCase(productRepository);
    }

    @Bean
    public UpdateProductUseCase updateProductUseCase(ProductRepository productRepository) {
        return new UpdateProductUseCase(productRepository);
    }

    @Bean
    public DeleteProductUseCase deleteProductUseCase(ProductRepository productRepository) {
        return new DeleteProductUseCase(productRepository);
    }

    @Bean
    public SearchProductUseCase searchProductUseCase(SearchRepository searchRepository) {
        return new SearchProductUseCase(searchRepository);
    }

    @Bean
    public GetRecommendationsUseCase getRecommendationsUseCase(RecommendationRepository recommendationRepository) {
        return new GetRecommendationsUseCase(recommendationRepository);
    }
}
